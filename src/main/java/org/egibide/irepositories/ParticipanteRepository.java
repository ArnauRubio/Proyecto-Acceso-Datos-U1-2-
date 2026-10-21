package org.egibide.irepositories;

import org.egibide.models.Participante;
import org.egibide.models.Partida;

public interface ParticipanteRepository {
    Participante getParticipante(int id_participante);
}
