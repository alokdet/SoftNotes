package com.example.softnotesbeta;

import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.softnotesbeta.WorkspaceControllers.NoteControllerHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class TextTranslator {

    private final String[] fromLanguages = {"Arabic", "Bengali", "German", "English", "Hindi", "Gujarati", "Marathi", "Tamil", "Telugu", "Korean", "Urdu"};
    private final String[] toLanguages = {"Arabic", "Bengali", "German", "English", "Hindi", "Gujarati", "Marathi", "Tamil", "Telugu", "Korean", "Urdu"};
    private String fromLanguage, toLanguage, fromLanguageCode, toLanguageCode;
    private String translatedText;

    public TextTranslator() {

    }

    public void translate(String text) {

        fromLanguageCode = getLanguageCode(fromLanguage);
        toLanguageCode = getLanguageCode(toLanguage);

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();
        final Translator translator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translator.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        NoteControllerHandler.getInstance().setTranslatedText(s);
                        translatedText = s;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public String getTranslatedText() {
        return this.translatedText;
    }

    public String getLanguageCode(String language) {
        String languageCode;

        switch (language) {
            case "English":
                languageCode = TranslateLanguage.ENGLISH;
                break;
            case "Hindi":
                languageCode = TranslateLanguage.HINDI;
                break;
            case "Gujarati":
                languageCode = TranslateLanguage.GUJARATI;
                break;
            case "Marathi":
                languageCode = TranslateLanguage.MARATHI;
                break;
            case "Tamil":
                languageCode = TranslateLanguage.TAMIL;
                break;
            case "Telugu":
                languageCode = TranslateLanguage.TELUGU;
                break;
            case "Bengali":
                languageCode = TranslateLanguage.BENGALI;
                break;
            case "Arabic":
                languageCode = TranslateLanguage.ARABIC;
                break;
            case "German":
                languageCode = TranslateLanguage.GERMAN;
                break;
            case "Korean":
                languageCode = TranslateLanguage.KOREAN;
                break;
            case "Urdu":
                languageCode = TranslateLanguage.URDU;
                break;
            default:
                languageCode = "";
        }

        return languageCode;
    }

    public String[] getLanguages() {
        return fromLanguages;
    }

    public void setToLanguage(String language) {
        this.toLanguage = language;
    }

    public void setFromLanguage(String language) {
        this.fromLanguage = language;
    }
}
