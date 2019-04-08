package com.glowingsoft.Recomendados.Seller.Models;

import java.io.Serializable;

public class LanguageModel implements Serializable {
    String id, title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
