package com.example.examplemod.voice;

import com.example.examplemod.network.CastFireballPacket;
import com.example.examplemod.network.PacketHandler;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import io.github.ggerganov.whispercpp.WhisperCpp;
import io.github.ggerganov.whispercpp.params.WhisperFullParams;
import io.github.ggerganov.whispercpp.params.WhisperSamplingStrategy;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WhisperRecognitionManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    static {
        System.setProperty("jna.library.path", "/Users/ootakeiyume/Documents/lets_minecraft/mod_create_magearena/libs");
    }
    // インスタンスを保持
    private static WhisperCpp whisper;
    private static TargetDataLine microphone;
    private static boolean isListening = false;
    private static Thread listeningThread;

    public static void initialize(String modelPath) {
        try {
            // 1. Whisperの初期化 (モデルの読み込み)
            File modelFile = new File(modelPath);
            if (!modelFile.exists()) {
                LOGGER.error("Whisperモデルファイルが見つかりません: " + modelPath);
                return;
            }

            // ライブラリのロード (JNA経由で libwhisper.dylib を読み込む)
            whisper = new WhisperCpp();
            // モデルを読み込んでコンテキストを作成
            whisper.initContext(modelPath);

            LOGGER.info("Whisperモデルのロードに成功しました: " + modelPath);

            // 2. マイクの初期化 (既存のコード)
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                LOGGER.error("マイクの形式がサポートされていません。");
                return;
            }

            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            LOGGER.info("マイクの初期化に成功しました！");

        } catch (Exception e) {
            LOGGER.error("初期化中にエラーが発生しました", e);
        }
    }

    public static void startListening() {
        if (isListening || whisper == null) return;
        isListening = true;
        microphone.start();

        listeningThread = new Thread(() -> {
            List<Float> audioBuffer = new ArrayList<>();
            byte[] readBuffer = new byte[4096];

            while (isListening) {
                int nbytes = microphone.read(readBuffer, 0, readBuffer.length);
                if (nbytes > 0) {
                    // 音量計算
                    double sum = 0;
                    int sampleCount = nbytes / 2;
                    short[] tempSamples = new short[sampleCount];
                    ByteBuffer.wrap(readBuffer, 0, nbytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(tempSamples);

                    for (short s : tempSamples) sum += s * s;
                    double rms = Math.sqrt(sum / sampleCount);

                    // しきい値を超えた時だけ追加
                    if (rms > 300) {
                        for (short s : tempSamples) {
                            audioBuffer.add(s / 32768.0f);
                        }
                    }
                    // 3秒以内に呪文言い終わらないと単語が切れてしまう
                    if (audioBuffer.size() >= 48000) {
                        performTranscription(audioBuffer);
                    }
                }
            }
        }, "WhisperThread");
        listeningThread.setDaemon(true);
        listeningThread.start();
    }
    public float[] bytesToFloats(byte[] bytes) {
        // 16bit(2bytes) で 1つの音の高さ(1sample)を表現しているので、長さは半分になる
        float[] floats = new float[bytes.length / 2];
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < floats.length; i++) {
            // 16bit short として読み込み、float の範囲（-32768〜32767）で割って -1.0〜1.0 に正規化
            short s = buffer.getShort();
            floats[i] = s / 32768.0f;
        }
        return floats;
    }
    public static void performTranscription(List<Float> audioBuffer) {
        if (audioBuffer.isEmpty()) return;
        float[] samples = new float[audioBuffer.size()];
        for (int i = 0; i < audioBuffer.size(); i++) samples[i] = audioBuffer.get(i);

        try {
            WhisperFullParams.ByValue params = whisper.getFullDefaultParams(WhisperSamplingStrategy.WHISPER_SAMPLING_GREEDY);
            params.initial_prompt = "Fire";
            params.language = "en";
            // 文字起こし
            String result = whisper.fullTranscribe(params, samples);

            if (result != null && !result.isEmpty()) {
                LOGGER.info("Final Result: " + result);
                // 小文字修正
                String query = result.toLowerCase();

                if (query.contains("fire")) {
                    LOGGER.info("★magic: fire detected!");
                    PacketHandler.INSTANCE.sendToServer(new CastFireballPacket());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Transcription error", e);
        }
        audioBuffer.clear();
    }
}