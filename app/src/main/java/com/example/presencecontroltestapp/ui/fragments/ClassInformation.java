package com.example.presencecontroltestapp.ui.fragments;

public class ClassInformation {
    private String name;
    private String professorName;
    private String diaDaSemana;
    private int qtdAulasDadas;
    private int qtdAulasAssistidas;

    public ClassInformation(String name, String professorName, int aulasDadas, int aulasAssistidas, String diaDaSemana) {
        this.name = name;
        this.professorName = professorName;
        this.qtdAulasDadas = aulasDadas;
        this.qtdAulasAssistidas = aulasAssistidas;
        this.diaDaSemana = diaDaSemana;
    }

    public String getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(String diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
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
                ", dia da semana='" + diaDaSemana + '\'' +
                '}';
    }
}
