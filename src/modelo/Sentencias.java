/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

/**
 *
 * @author Usuario
 */
public enum Sentencias {
    
    
    INSERTARIGACCATASTRO("INSERT INTO Catastro (departamentoidfk,municipioidfk,numpredio,avaluo,sector,manzana,predio,propiedad,matricula,direccion,tipo,estado,fechaRecibido) VALUES\n" +
        " ('','','','','','','','','','','','','') RETURNING catastroid;"),
    INSERTARIGACREGISTRO("INSERT INTO registro (matriculaori,predialori,direccion,tipo,estado,fecharecibido,municipioidfk,departamentoidfk) VALUES\n" +
        " ('','','','','','','','') RETURNING registroid;"),
    INSERTARIGACITR("INSERT INTO ITR (departamentoidfk,municipioidfk,fecharecibido,numpredio,avaluos,sector,manzana,predio,propiedad,circulo,matricula,areaterreno,areaconstruida,direccioncat,direccionreg,crpredial,crmatricula,crdireccion,crdocumento,crnombre) VALUES\n" +
        " ('','','','','','','','','','','','','','','','','','','') RETURNING itrid"),
    INSERTARIGACCATASTROPROPIETARIO("INSERT INTO catastro_propietarios (catastroid_Catastro,propietarioid_propietarios) VALUES\n" +
        " ('','');"),
    INSERTARIGACREGISTROPROPIETARIO(" INSERT INTO registro_propietarios (registroid_registro,propietarioid_propietarios) VALUES\n" +
        " ('','');"),
    INSERTARIGACITRCATPROP(" INSERT INTO itr_cat_propietario (itrid_ITR,propietarioid_propietarios) VALUES\n" +
        " ('','');"),
    INSERTARIGACITRREGPROP(" INSERT INTO itr_reg_propietario (itrid_ITR,propietarioid_propietarios) VALUES\n" +
        " ('','');"),
    INSERTARANTIOQUIACATASTRO(" INSERT INTO catastro_gobAnt (municipioidfk,departamentoidfk,predial,predialcat28,predialcat30,avaluo,corregimiento,matriculaori,direccion,tipo,estado,fecharecibido) VALUES\n" +
        " ('','','','','','','','','','','','') RETURNING catastroid;"),
    INSERTARANTIOQUIACATASTROPROP("INSERT INTO cat_gobant_prop (catastroid_catastro_gobAnt,propietarioid_propietarios) VALUES\n" +
        " ('','');"),
    INSERTARANTIOQUIAREGISTRO("INSERT INTO registro_gobant (matriculaori,predialori,direccion,tipo,estado,municipioidfk,fecharecibido) VALUES\n" +
        " ('','','','','','','') RETURNING registroid;"),
    INSERTARANTIOQUIAREGISTROPROP(" INSERT INTO reg_gobant_prop (registroid_registro_gobant,propietarioid_propietarios) VALUES\n" +
        " ('','');"),
    INSERTARANTIOQUIAITR("INSERT INTO itr_gobant (departamentoidfk,municipioifk,numpredio,predialcat,predialcat28,predialcat30,avaluo,corregimiento,circulo,matricula,areaterreno,areaconstruida,direccioncat,direccionreg,crpredial,crmatricula,crdireccion,crdocumento,crnombre,fecharecibido) VALUES\n" +
        " ('','','','','','','','','','','','','','','','','','','','') RETURNING itrgobantid;"),
    INSERTARANTIOQUIAITRCATASTROPROP("INSERT INTO itr_catgobant_prop (itrgobantid_itr_gobant,propietarioid_propietarios) VALUES\n" +
        " ('','');"),
    INSERTARANTIOQUIAITRREGISTROPROP("INSERT INTO itr_reggobant_prop (itrgobantid_itr_gobant,propietarioid_propietarios) VALUES\n" +
        " ('','');"),
    INSERTARMEDELLINCATASTRO("INSERT INTO catastro_medellin (idpredio,numpredial,areaterreno,areaconstruida,direccion,tipopredio) VALUES\n" +
        " ('','','','','','') RETURNING catastromedid;"),
    INSERTARMEDELLINREGISTRO(" INSERT INTO registro_medellin (circulo,matricula,direccion,tipopredio) VALUES\n" +
        " ('','','','') RETURNING registromedid;"),
    INSERTARMEDELLINITR("INSERT INTO itr_medellin (idpredio,numpredio,matricula,areaterreno,areaconstruida,direccioncat,direccionreg,tipopredio,crpredial,crmatricula,crdireccion,crdocumento,crnombre,fecharecibido) VALUES\n" +
        " ('','','','','','','','','','','','','','') RETURNING itrmedid;"),
    VERIFICARPROPIETARIO("select propietarioid from propietarios where tipodoc like ? and numdoc like ?"),
    INSERTARPROPIETARIO("INSERT INTO propietarios (tipodoc,numdoc,nombrepropietario) VALUES\n" +
        " ('','','') RETURNING propietarioid;");
    

    private Sentencias(String sentencia) {
        this.sentencia=sentencia;
    }
    
    private final String sentencia;
    
    public String getSentencia(){
        return sentencia;
    }
}
