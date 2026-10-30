package org.egibide.dao;

import org.egibide.models.Asociacion;
import org.egibide.models.Socia;

import java.sql.SQLException;
import java.util.List;

/**
 * Operaciones de persistencia y consulta de socias.
 */
public interface SociaDao {

    /**
     * Busca una socia por su identificador.
     *
     * @param id identificador de la socia
     * @return socia encontrada o null si no existe
     */
    Socia getSocia(int id);

    /**
     * Recupera todas las socias registradas.
     *
     * @return lista de socias, vacía si no hay resultados
     */
    List<Socia> getSocias();

    /**
     * Inserta una socia mediante la operación básica del DAO.
     *
     * @param socia datos que se insertarán
     * @return número de filas afectadas
     */
    int addSocia(Socia socia);

    /**
     * Inserta una socia y guarda la asociación indicada.
     *
     * @param socia      datos de la socia
     * @param asociacion asociación a la que se vinculará
     * @return número de filas afectadas
     * @throws SQLException si falla la consulta a la base de datos
     */
    int insertSocia(Socia socia, Asociacion asociacion) throws SQLException;

    /**
     * Actualiza los datos y la asociación de una socia.
     *
     * @param socia      datos actualizados de la socia
     * @param asociacion nueva asociación
     * @return {@code true} si se actualizó alguna fila
     * @throws SQLException si falla la consulta a la base de datos
     */
    boolean updateSocia(Socia socia, Asociacion asociacion) throws SQLException;

    /**
     * Completa las asociaciones de las socias ya cargadas.
     *
     * @param socias socias a relacionar con sus asociaciones
     * @throws SQLException si falla la consulta a la base de datos
     */
    void loadSociaAssociations(List<Socia> socias) throws SQLException;

    /**
     * Actualiza una socia mediante la operación básica del DAO.
     *
     * @param socia datos actualizados
     * @return {@code true} si se modificó alguna fila
     */
    boolean update(Socia socia);

    /**
     * Elimina una socia por su identificador.
     *
     * @param id identificador de la socia que se eliminará
     */
    void delete(int id);

    /**
     * Busca la socia asociada a una participación.
     *
     * @param id_participante identificador de la participación
     * @return socia encontrada o null si no existe
     */
    Socia getSociaByParticipanteId(int id_participante);

}
