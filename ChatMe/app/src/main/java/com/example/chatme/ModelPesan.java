package com.example.chatme;

public class ModelPesan {
    private String pesan;
    private String pengirim;

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public ModelPesan (String pesan, String pengirim) {
        this.pesan = pesan;
        this.pengirim = pengirim;
    }
}
