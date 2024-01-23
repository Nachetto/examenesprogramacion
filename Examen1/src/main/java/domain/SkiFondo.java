package domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

public class SkiFondo extends Pista implements Serializable{
    @Getter @Setter
    private List<String> pueblos;
    public SkiFondo(String nombre, String provincia, int id, int km, List<String> pueblos) {
        super(nombre, provincia, id, km);
        this.pueblos=pueblos;
    }

    @Override
    public String toString() {
        return "Tipo: SkiFondo \n" +
                "Id: " + id +"\n" +
                "Provincia: " + provincia +"\n"+
                "Nombre: " + nombre +  "\n"+
                "Pueblos: " + pueblos + "\n" +
                "km: " + km+"\n\n";
    }

    @Override
    public String toStringTextFile() {
        StringBuilder pueblosString= new StringBuilder();
        //pueblos.forEach(p->pueblosString.append(p).append(","));
        for (String pueblo:pueblos) {
            pueblosString.append(pueblo).append(",");
        }
        return "SkiFondo;"+nombre+";"+provincia+";"+id+";"+km+";"+pueblosString+"\n";
    }
}
