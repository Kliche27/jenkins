package services;

import java.util.List;

public interface Service<T>{
    void ajouter(T t);
    void modifier(T t);
    void supprimer(int id);
    List<T> rechercher();
}
