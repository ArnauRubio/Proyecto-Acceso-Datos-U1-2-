package org.egibide.dao;

import org.egibide.models.Participante;
import org.egibide.models.Socia;

import java.util.List;

public interface ParticipanteDao {
    Participante getParticipante(int id);

    List<Participante> getParticipantes();

    int addParticipante(Participante participante);

    boolean update(Participante participante);

    void delete(int id);

    List<Participante> getParticipantesByPartidaId(int id_partida);

    List<Participante> getGanadorasByPartidaId(int id_partida);

    List<Participante> getParticipacionesBySociaId(int id_socia);
}
