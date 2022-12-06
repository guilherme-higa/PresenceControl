package com.example.presencecontroltestapp.ui.fragments;

public class ClassInformation {
    private String name;
    private String professorName;
    private int qtdAulasDadas;
    private int qtdAulasAssistidas;

    public ClassInformation(String name, String professorName, int aulasDadas, int aulasAssistidas) {
        this.name = name;
        this.professorName = professorName;
        this.qtdAulasDadas = aulasDadas;
        this.qtdAulasAssistidas = aulasAssistidas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public int getQtdAulasDadas() {
        return qtdAulasDadas;
    }

    public void setQtdAulasDadas(int qtdAulasDadas) {
        this.qtdAulasDadas = qtdAulasDadas;
    }

    public int getQtdAulasAssistidas() {
        return qtdAulasAssistidas;
    }

    public void setQtdAulasAssistidas(int qtdAulasAssistidas) {
        this.qtdAulasAssistidas = qtdAulasAssistidas;
    }

    @Override
    public String toString() {
        return "ClassInformation{" +
                "name='" + name + '\'' +
                ", professor='" + professorName + '\'' +
                ", quantidades de aulas dadas='" + qtdAulasDadas + '\'' +
                ", quantidade de aulas assistidas='" + qtdAulasAssistidas + '\'' +
                '}';
    }
}
