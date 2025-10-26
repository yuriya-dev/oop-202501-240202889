package main.java.com.upb.agripos.model;

public class ObatHama extends Produk {
    private String jenisHama;

    public ObatHama(String kode, String nama, double harga, int stok, String jenisHama) {
        super(kode, nama, harga, stok);
        this.jenisHama = jenisHama;
    }

    @Override
    public String getInfo() {
        return "Obat Hama: " + super.getInfo() + ", Jenis Hama: " + jenisHama;
    }
}
