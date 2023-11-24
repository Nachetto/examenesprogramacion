package dao;

import domain.Pista;

import java.util.List;

public interface PistasDao{
    List<Pista> getPistas();
    boolean addPista(Pista p);
    boolean removePista(Pista p);
    int kmExtensionPorProvincia(String provincia);
    boolean nuevoPuebloParaPista(String nombreDeLaPista, String nuevoPueblo);
    List<Pista> obtenerPistasOrdenadasPorProvinciaYKm();
    boolean eliminarPistaPorId(int idPista);
    boolean escribirFichero();
    boolean escribirBinario();
    boolean cargarBinario();
    void mapa();
    boolean cargarFicheroTexto();
}
