package main;

import common.ExcepcionDificultad;
import domain.SkiAlpino;
import domain.SkiFondo;
import service.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainUI {
    private final Service service = new Service();

    public static void main(String[] args) {
        MainUI mainUI = new MainUI();
        mainUI.menu();
    }

    private void menu() {
        int resultado = 0;
        while (resultado != 12) {
            resultado = 0;
            System.out.println(ConstantesMain.MENUINICIO);
            Scanner sc = new Scanner(System.in);
            try {
                resultado = sc.nextInt();
            } catch (Exception e) {
                System.out.println(ConstantesMain.ERRORMENU);
            } finally {
                sc.nextLine();
            }
            switch (resultado) {
                case 1 -> listaDeProvincias();

                case 2 -> pistasOrdenadasPorProvinciaYKm();

                case 3 -> nuevaPista(sc);

                case 4 -> kmExtensionPorProvincia(sc);

                case 5 -> nuevoPuebloParaPista(sc);

                case 6 -> eliminarPistaPorId(sc);

                case 7 -> escribirFicheroTexto();

                case 8 -> escribirFicheroBinario();

                case 9 -> cargarFicheroBinario();

                case 10 -> cargarFicheroTexto();

                case 11 -> mapa();

                case 12 -> System.out.println("Hasta Luego");

                default -> System.out.println(ConstantesMain.ERRORMENUINICIO);
            }
        }
    }

    private void mapa() {
        //11. Mapa que para cada provincia lista de pistas que existen
        System.out.println("Opcion 11");
        service.mapa();
    }

    private void cargarFicheroTexto() {
        //10. Cargar el fichero de texto
        System.out.println("Opcion 10");
        service.cargarFicheroTexto();
        if (service.cargarFicheroTexto())
            System.out.println("Fichero cargado correctamente");
    }
    private void cargarFicheroBinario() {
        //9. Cargar el binario
        System.out.println("Opcion 9");
        if (service.cargarBinario())
            System.out.println("Binario cargado correctamente");
        else
            System.out.println(ConstantesMain.ERRORBINARIO);
    }

    private void escribirFicheroBinario() {
        //8. Pasar a un binario todas las pistas
        System.out.println("Opcion 8");
        if (service.escribirBinario())
            System.out.println("Binario escrito correctamente");
        else
            System.out.println(ConstantesMain.ERRORBINARIOESCRITURA);
    }

    private void escribirFicheroTexto() {
        //7. Pasar a un fichero de texto todas las pistas
        System.out.println("Opcion 7");
        if (service.escribirFichero())
            System.out.println("Fichero escrito correctamente");
        else
            System.out.println(ConstantesMain.ERRORFICHEROESCRITURA);
    }

    private void eliminarPistaPorId(Scanner sc) {
        //6. Eliminar una pista por id
        System.out.println("Opcion 6. Introduce el id de la pista");
        int idPista = 0;
        while (idPista == 0) {
            try {
                idPista = sc.nextInt();
                if (service.eliminarPistaPorId(idPista))
                    System.out.println("Pista eliminada");
                else
                    System.out.println("No se ha podido eliminar la pista");
            } catch (Exception e) {
                System.out.println(ConstantesMain.ERRORID);
                idPista = 0;
            } finally {
                sc.nextLine();
            }
        }
    }

    private void nuevoPuebloParaPista(Scanner sc) {
        //5. Añadir un nuevo pueblo a una pista de ski de fondo
        System.out.println("Opcion 5. Introduce el nombre de la pista");
        String nombrePista = sc.next();
        System.out.println("Introduce el nombre del pueblo");
        String nombrePueblo = sc.next();
        System.out.println(service.nuevoPuebloParaPista(nombrePista, nombrePueblo));
    }

    private void kmExtensionPorProvincia(Scanner sc) {
        //4. Ver km de extension por provincia
        System.out.println("Opcion 4. Introduce la provincia");
        String provincia = sc.next();
        System.out.println(service.kmExtensionPorProvincia(provincia));
    }

    private void nuevaPista(Scanner sc) {
        //3. Añadir una nueva pista
        int opcion = 0;
        while (opcion == 0) {
            try {
                System.out.println("Opcion 3. De que tipo es la pista que quieres añadir?");
                System.out.println("1. SkiAlpino");
                System.out.println("2. SkiFondo");
                System.out.println("3. Salir");
                opcion = sc.nextInt();


                System.out.println(ConstantesMain.NOMBRE_DE_LA_PISTA);
                String nombre = sc.next();
                System.out.println("Introduce la provincia de la pista");
                String provincia = sc.next();
                System.out.println("Introduce el id de la pista");
                int id = sc.nextInt();
                sc.nextLine();
                System.out.println("Introduce los km de la pista");
                int km = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1 -> {
                        try {
                            if (nuevaSkiAlpino(sc, nombre, provincia, id, km))
                                System.out.println("Pista añadida correctamente");
                            else
                                System.out.println("No se ha podido añadir la pista");
                        } catch (ExcepcionDificultad e) {
                            System.out.println(e.getMessage());
                            opcion = 0;
                        }
                    }
                    case 2 -> {
                        System.out.println("Introduce los pueblos de la pista separados por espacios");
                        List<String> pueblos = Arrays.asList(sc.nextLine().split(" "));
                        SkiFondo skiFondo = new SkiFondo(nombre, provincia, id, km, pueblos);
                        if (service.addPista(skiFondo))
                            System.out.println("Pista añadida correctamente");
                        else
                            System.out.println("No se ha podido añadir la pista");
                    }
                    case 3 -> System.out.println("Saliendo...");
                    default -> {
                        System.out.println("Opcion incorrecta");
                        opcion = 0;
                    }
                }
            } catch (Exception e) {
                sc.nextLine();
                System.out.println(ConstantesMain.ERRORMENU);
                opcion = 0;
            }
        }
    }

    private boolean nuevaSkiAlpino(Scanner sc, String nombre, String provincia, int id, int km) throws ExcepcionDificultad {
            System.out.println("Introduce la dificultad de la pista");
            String dificultad = sc.next();
            SkiAlpino skiAlpino = new SkiAlpino(nombre, provincia, id, km, dificultad);
            return service.addPista(skiAlpino);
    }

    private void pistasOrdenadasPorProvinciaYKm() {
        //2. Listado ordenado de pistas por provincia
        System.out.println("Opcion 2");
        System.out.println(service.obtenerPistasOrdenadasPorProvinciaYKm());
    }

    private void listaDeProvincias() {
        //1. Lista de provincias
        System.out.println("Opcion 1");
        System.out.println(service.getPistas().toString());
    }
}
