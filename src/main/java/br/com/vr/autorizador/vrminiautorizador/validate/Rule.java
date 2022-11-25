package br.com.vr.autorizador.vrminiautorizador.validate;

public interface Rule<T, U> {
    boolean validate(T objeto, U comparavel);

    void error();
}
