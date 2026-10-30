package org.egibide.repositories;

import org.egibide.dao.JuegoDao;
import org.egibide.dao.ParticipanteDao;
import org.egibide.dao.PartidaDao;
import org.egibide.dao.SociaDao;
import org.egibide.idao.JuegoDaoImpl;
import org.egibide.idao.ParticipanteDaoImpl;
import org.egibide.idao.PartidaDaoImpl;
import org.egibide.idao.SociaDaoImpl;
import org.egibide.irepositories.ParticipanteRepository;
import org.egibide.irepositories.PartidaRepository;
import org.egibide.models.Juego;
import org.egibide.models.Participante;
import org.egibide.models.Partida;
import org.egibide.models.Socia;

import java.util.List;

/**
 * Implementación del repositorio que completa las relaciones de cada participación.
 */
public class ParticipanteRepositoryImpl implements ParticipanteRepository {

    private SociaDao sociaDao = new SociaDaoImpl();
    private ParticipanteDao participanteDao = new ParticipanteDaoImpl();
    private PartidaDao partidaDao = new PartidaDaoImpl();

    /**
     * Recupera la participación junto con la socia y la partida relacionadas.
     */
    @Override
    public Participante getParticipante(int id_participante) {
        Participante participante = participanteDao.getParticipante(id_participante);

        Socia socia = sociaDao.getSociaByParticipanteId(participante.getId());
        participante.setSocia(socia);

        Partida partida = partidaDao.getPartidaByParticipanteId(participante.getId());
        participante.setPartida(partida);

        return participante;
    }
}
