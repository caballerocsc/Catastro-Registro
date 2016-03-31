/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package obj;

/**
 * Clase que permite mapear los archivos de excel a objetos java,
 * para luego ser guardados en la base de datos
 * @author cesar solano
 */
public class Registro {

    public Registro() {
    }
    
    private int registroid;
    private String matriculaori;
    private String predialori;
    private String direccion;
    private String tipo;
    private String estado;
    private String fecharecibido;
    private String municipioidfk;
    private String departamentoidfk;
    private String circulo;

    public int getRegistroid() {
        return registroid;
    }

    public void setRegistroid(int registroid) {
        this.registroid = registroid;
    }

    public String getMatriculaori() {
        return matriculaori;
    }

    public void setMatriculaori(String matriculaori) {
        this.matriculaori = matriculaori;
    }

    public String getPredialori() {
        return predialori;
    }

    public void setPredialori(String predialori) {
        this.predialori = predialori;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecharecibido() {
        return fecharecibido;
    }

    public void setFecharecibido(String fecharecibido) {
        this.fecharecibido = fecharecibido;
    }

    public String getMunicipioidfk() {
        return municipioidfk;
    }

    public void setMunicipioidfk(String municipioidfk) {
        this.municipioidfk = municipioidfk;
    }

    public String getDepartamentoidfk() {
        return departamentoidfk;
    }

    public void setDepartamentoidfk(String departamentoidfk) {
        this.departamentoidfk = departamentoidfk;
    }

    public String getCirculo() {
        return circulo;
    }

    public void setCirculo(String circulo) {
        this.circulo = circulo;
    }
    
    
    
}
