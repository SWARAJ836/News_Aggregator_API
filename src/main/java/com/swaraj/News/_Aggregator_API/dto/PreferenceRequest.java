package com.swaraj.News._Aggregator_API.dto;

import jakarta.validation.constraints.NotBlank;

public class PreferenceRequest {
    @NotBlank
    private String preferences;

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
