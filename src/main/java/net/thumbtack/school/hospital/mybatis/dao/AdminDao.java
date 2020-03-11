package net.thumbtack.school.hospital.mybatis.dao;


import net.thumbtack.school.hospital.model.Admin;


public interface AdminDao {

    Admin insert(Admin admin);

    Admin getById(int id);
//
//    // получает список всех Trainee, независимо от их Group. Если БД не содержит ни одного Trainee, метод возвращает пустой список
//    List<Trainee> getAll();
//
//    // изменяет Trainee в базе данных. Метод не изменяет принадлежности Trainee к Group
//    Trainee update(Trainee trainee);
//
//    // получает список всех Trainee при условиях
//    // если firstName не равен null - только имеющих это имя
//    // если lastName не равен null - только имеющих эту фамилию
//    // если rating не равен null - только имеющих эту оценку
//    List<Trainee> getAllWithParams(String firstName, String lastName, Integer rating);
//
//    // вставляет список из Trainee в базу данных. Вставленные Trainee не принадлежат никакой группе
//    void batchInsert(List<Trainee> trainees);
//
//    // удаляет Trainee из базы данных
//    void delete(Trainee trainee);
//
    // удаляет все Trainee из базы данных
    void deleteAll();


}
