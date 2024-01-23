package dao;

import common.Constantes;
import common.ExcepcionDificultad;
import domain.Pista;
import domain.SkiAlpino;
import domain.SkiFondo;

import java.io.*;
import java.util.*;

public class PistasDaoImpl implements PistasDao {
    private List<Pista> pistas;

    public PistasDaoImpl() {
        this.pistas = new ArrayList<>();
        List<String> nombresPueblo1 = Arrays.asList("Pueblo1", "Pueblo2", "Pueblo3");
        List<String> nombresPueblo2 = Arrays.asList("Pueblo4", "Pueblo5");
        List<String> nombresPueblo3 = Arrays.asList("Pueblo6", "Pueblo7", "Pueblo8");
        List<String> nombresPueblo4 = Arrays.asList("Pueblo9", "Pueblo10");

        try {
            pistas.add(new SkiAlpino("Aliga", "Madrid", 1, (int) (Math.random() * 10) + 1, "azul"));
            pistas.add(new SkiAlpino("Diosmio", "Galicia", 2, (int) (Math.random() * 10) + 1, "verde"));
            pistas.add(new SkiAlpino("Quemecaigo", "Burgos", 3, (int) (Math.random() * 10) + 1, "roja"));
            pistas.add(new SkiAlpino("Auxilio", "Valencia", 4, (int) (Math.random() * 10) + 1, "azul"));
            pistas.add(new SkiFondo("Nosalgovivo", "Alicante", 5, (int) (Math.random() * 10) + 1, nombresPueblo1));
            pistas.add(new SkiFondo("Gaviota", "Granada", 6, (int) (Math.random() * 10) + 1, nombresPueblo2));
            pistas.add(new SkiFondo("Perro", "Huelva", 7, (int) (Math.random() * 10) + 1, nombresPueblo3));
            pistas.add(new SkiFondo("Gato", "Sevilla", 8, (int) (Math.random() * 10) + 1, nombresPueblo4));
        } catch (Exception e) {
            System.out.println(Constantes.PISTASINICIALES + e.getMessage());
        }
    }

    @Override
    public List<Pista> getPistas() {
        return pistas;
    }

    @Override
    public boolean addPista(Pista p) {
        return pistas.add(p);
    }

    @Override
    public boolean removePista(Pista p) {
        return pistas.remove(p);
    }


    @Override
    public int kmExtensionPorProvincia(String provincia) {
        return pistas.stream()
                .filter(pista -> pista.getProvincia().equalsIgnoreCase(provincia))
                .mapToInt(Pista::getKm).sum();
    }


    @Override
    public boolean nuevoPuebloParaPista(String nombreDeLaPista, String nuevoPueblo) {
        //busco em todas las pistas y si es de ski de fondo añado el pueblo
        for (Pista pista : pistas) {
            if (pista.getNombre().equalsIgnoreCase(nombreDeLaPista)) {
                try {
                    return ((SkiFondo) pista).getPueblos().add(nuevoPueblo);
                } catch (Exception e) {
                    System.out.println(Constantes.ERRORSKIFONDO);
                    return false;
                }
            }
        }
        return false;
    }


    @Override
    public List<Pista> obtenerPistasOrdenadasPorProvinciaYKm() {
        List<Pista> copiaPistas = new ArrayList<>(pistas);
        Collections.sort(copiaPistas, new Comparator<Pista>() {
            @Override
            public int compare(Pista pista1, Pista pista2) {
                // Compara por provincia
                int comparacionPorProvincia = pista1.getProvincia().compareTo(pista2.getProvincia());
                if (comparacionPorProvincia == 0) {
                    // Si las provincias son iguales, compara por kilómetros
                    return Integer.compare(pista1.getKm(), pista2.getKm());
                }
                return comparacionPorProvincia;
            }
        });
        return copiaPistas;
    }

    @Override
    public boolean eliminarPistaPorId(int idPista) {
        for (Pista pista : pistas) {
            if (pista.getId() == idPista) {
                return removePista(pista);
            }
        }
        return false;
    }


    @Override
    public boolean escribirFichero() {
        try (FileWriter fichero = new FileWriter("src//main//resources//FicheroTXT.txt")) {
            for (Pista pista : pistas) {
                fichero.write(pista.toStringTextFile());
            }
            return true;
        } catch (IOException e) {
            System.out.println(Constantes.ERRORFICHERO + " porque:" + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean escribirBinario() {
        try (FileOutputStream archivoBinario = new
                FileOutputStream("src//main//resources//FicheroBIN");
             ObjectOutputStream escribir = new
                     ObjectOutputStream(archivoBinario)) {
            escribir.writeObject(pistas);//un arraylist
            return true;
        } catch (IOException e) {
            System.out.println(Constantes.ERRORBINARIO + " porque:" + e.getMessage());
            return false;
        }
    }


    @Override
    public void mapa() {
        Map<String, List<Pista>> mapa = new HashMap<>();
        for (Pista pista : pistas) {
            if (mapa.containsKey(pista.getProvincia())) {
                mapa.get(pista.getProvincia()).add(pista);
            } else {
                List<Pista> listaPistas = new ArrayList<>();
                listaPistas.add(pista);
                mapa.put(pista.getProvincia(), listaPistas);
            }
        }
        for (Map.Entry<String, List<Pista>> entry : mapa.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


    @Override
    public boolean cargarFicheroTexto() {
        Boolean resultado=true;
        try (FileReader fichero = new FileReader("src//main//resources//FicheroTXT.txt");
             BufferedReader lector = new BufferedReader(fichero)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos[0] .equals("SkiAlpino")) {
                    try {
                        pistas.add(new SkiAlpino(datos[1], datos[2], Integer.parseInt(datos[3]), Integer.parseInt(datos[4]), datos[5]));
                        resultado=true;
                    } catch (ExcepcionDificultad excepcionDificultad) {
                        System.out.println(excepcionDificultad.getMessage());
                        resultado=false;
                    }
                } else if (datos[0].equals("SkiFondo")) {
                    pistas.add(new SkiFondo(datos[1], datos[2], Integer.parseInt(datos[3]), Integer.parseInt(datos[4]), Arrays.asList(datos[5].split(","))));
                    resultado=true;
                }
            }
        } catch (IOException e) {
            System.out.println(Constantes.ERRORFICHERO + " porque:" + e.getMessage());
            resultado=false;
        }
        return resultado;
    }


    @Override
    public boolean cargarBinario() {
        try (FileInputStream archivoEntrada = new
                FileInputStream("src//main//resources//FicheroBIN");
             ObjectInputStream lectorObjeto = new
                     ObjectInputStream(archivoEntrada)) {
            pistas = (ArrayList<Pista>) lectorObjeto.readObject();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
