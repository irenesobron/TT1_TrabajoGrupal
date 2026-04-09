package java.com.ejemplo;

import java.util.List;

public class Solicitud {
    private List<Integer> cantidadesIniciales;

    private List<String> nombreEntidades;


    public List<Integer> getCantidadesIniciales() {

        return cantidadesIniciales;

    }



    public void setCantidadesIniciales(List<Integer> cantidadesIniciales) {

        this.cantidadesIniciales = cantidadesIniciales;

    }



    public List<String> getNombreEntidades() {

        return nombreEntidades;

    }



    public void setNombreEntidades(List<String> nombreEntidades) {

        this.nombreEntidades = nombreEntidades;

    }
}
