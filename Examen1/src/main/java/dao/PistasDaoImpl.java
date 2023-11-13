package dao;

import common.Constantes;
import common.ExcepcionDificultad;
import domain.Pista;
import domain.SkiAlpino;
import domain.SkiFondo;
import main.ConstantesMain;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PistasDaoImpl implements PistasDao{
    private List<Pista> pistas;

    public PistasDaoImpl() {
        this.pistas = new ArrayList<>();
        List<String> nombresPueblo1 = Arrays.asList("Pueblo1", "Pueblo2", "Pueblo3");
        List<String> nombresPueblo2 = Arrays.asList("Pueblo4", "Pueblo5");
        List<String> nombresPueblo3 = Arrays.asList("Pueblo6", "Pueblo7", "Pueblo8");
        List<String> nombresPueblo4 = Arrays.asList("Pueblo9", "Pueblo10");
        try{
            pistas.add(new SkiAlpino("Aliga","Madrid",1, (int) (Math.random() * 10) + 1,"azul"));
            pistas.add(new SkiAlpino("Diosmio","Galicia",2, (int) (Math.random() * 10) + 1,"verde"));
            pistas.add(new SkiAlpino("Quemecaigo","Burgos",3, (int) (Math.random() * 10) + 1,"roja"));
            pistas.add(new SkiAlpino("Auxilio","Valencia",4, (int) (Math.random() * 10) + 1,"azul"));
            pistas.add(new SkiFondo("Nosalgovivo", "Alicante", 5, (int) (Math.random() * 10) + 1, nombresPueblo1));
            pistas.add(new SkiFondo("Gaviota", "Granada", 6, (int) (Math.random() * 10) + 1, nombresPueblo2));
            pistas.add(new SkiFondo("Perro", "Huelva", 7, (int) (Math.random() * 10) + 1, nombresPueblo3));
            pistas.add(new SkiFondo("Gato", "Sevilla", 8, (int) (Math.random() * 10) + 1, nombresPueblo4));
        }catch (Exception e){
            System.out.println(Constantes.PISTASINICIALES +e.getMessage());
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

    public int kmExtensionPorProvincia(String provincia) {
        return pistas.stream()
                .filter(pista -> pista.getProvincia().equalsIgnoreCase(provincia))
                .mapToInt(Pista::getKm).sum();
    }

    public boolean nuevoPuebloParaPista(String nombreDeLaPista, String nuevoPueblo) {
        for ( Pista pista : pistas) {
            if (pista.getNombre().equalsIgnoreCase(nombreDeLaPista)) {
                try{
                    return ((SkiFondo) pista).getPueblos().add(nuevoPueblo);
                }catch (Exception e){
                    System.out.println(Constantes.ERRORSKIFONDO);
                    return false;
                }
            }
        }return false;
    }

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
    public boolean removePista(Pista p) {
        return pistas.remove(p);
    }

    public boolean eliminarPistaPorId(int idPista) {
        for (Pista pista : pistas) {
            if (pista.getId() == idPista) {
                return pistas.remove(pista);
            }
        }
        return false;
    }

    public boolean escribirFichero() {
        try {
            FileWriter fichero = new FileWriter(ConstantesMain.NOMBREFICHERO);
            for (Pista pista : pistas) {
                fichero.write(pista.toStringTextFile());
            }
            fichero.close();
            return true;
        } catch (IOException e) {
            System.out.println(Constantes.ERRORFICHERO+" porque:"+e.getMessage());
            return false;
        }
    }

    public boolean escribirBinario() {
        try {
            FileOutputStream archivoBinario = new
                    FileOutputStream("src//main//resources//FicheroBIN");
            ObjectOutputStream escribir = new
                    ObjectOutputStream(archivoBinario);
            escribir.writeObject(pistas);//un arraylist
            archivoBinario.close();
            escribir.close();
            return true;
        } catch (IOException e) {
            System.out.println(Constantes.ERRORBINARIO+" porque:"+e.getMessage());
            return false;
        }
    }


    public boolean cargarBinario() {
        try {
            FileInputStream archivoEntrada = new
                    FileInputStream("src//main//resources//FicheroBIN");
            ObjectInputStream lectorObjeto = new
                    ObjectInputStream(archivoEntrada);
            pistas = (ArrayList<Pista>) lectorObjeto.readObject();
            lectorObjeto.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public boolean cargarTxt(String nombreArchivo) {
        // Abre el archivo de texto para lectura
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Procesa la línea para crear un objeto Pista
                String[] datos = linea.split(";");
                if (datos[0].equals("SkiAlpino")) {
                    // Crea un objeto SkiAlpino y lo añade a la lista
                    SkiAlpino pistaAlpino = new SkiAlpino(datos[3], datos[2], Integer.parseInt(datos[1]), Integer.parseInt(datos[5]), datos[4]);
                    pistas.add(pistaAlpino);
                } else if (datos[0].equals("SkiFondo")) {
                    // Para SkiFondo necesitas convertir el String de pueblos a una lista
                    List<String> pueblos = Arrays.asList(datos[4].split(","));
                    SkiFondo pistaFondo = new SkiFondo(datos[3], datos[2], Integer.parseInt(datos[1]), Integer.parseInt(datos[5]), pueblos);
                    pistas.add(pistaFondo);
                }
            }
            return true;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
            return false;
        } catch (ExcepcionDificultad e) {
            System.out.println("Dificultad no válida: " + e.getMessage());
            return false;
        }

//        public Map<String, List<Pista>> getPistasPorProvincia() {
//            return pistas.stream()
//                    .collect(Collectors.groupingBy(Pista::getProvincia));
//        }


    }
}
