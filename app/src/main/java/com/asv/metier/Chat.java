package com.asv.metier;

/**
 * Created by Bertraior on 24/06/2015.
 */
public class Chat {
    private int idforumasv;
    private String pseudoasv;
    private String nomforumasv;
    private String datecreationforumasv;
    private int nbparticipant;

    public String getPseudoasv() {
        return pseudoasv;
    }

    public void setPseudoasv(String pseudoasv) {
        this.pseudoasv = pseudoasv;
    }

    public int getIdforumasv() {
        return idforumasv;
    }

    public void setIdforumasv(int idforumasv) {
        this.idforumasv = idforumasv;
    }

    public String getNomforumasv() {
        return nomforumasv;
    }

    public void setNomforumasv(String nomforumasv) {
        this.nomforumasv = nomforumasv;
    }

    public String getDatecreationforumasv() {
        return datecreationforumasv;
    }

    public void setDatecreationforumasv(String datecreationforumasv) {
        this.datecreationforumasv = datecreationforumasv;
    }

    public int getNbparticipant() {
        return nbparticipant;
    }

    public void setNbparticipant(int nbparticipant) {
        this.nbparticipant = nbparticipant;
    }
}
