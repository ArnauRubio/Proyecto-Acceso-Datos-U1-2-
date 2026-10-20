package org.egibide.repositories;

import org.egibide.dao.*;
import org.egibide.idao.*;
import org.egibide.irepositories.PartidaRepository;
import org.egibide.irepositories.SociaRepository;
import org.egibide.models.*;

import java.util.List;

public class PartidaRepositoryImpl implements PartidaRepository {

    private JuegoDao juegoDao = new JuegoDaoImpl();
    private PartidaDao partidaDao = new PartidaDaoImpl();
    private SociaDao sociaDao = new SociaDaoImpl();
    private ParticipanteDao participanteDao = new ParticipanteDaoImpl();

    @Override
    public Partida getPartida(int id_partida) {
        Partida partida = partidaDao.getPartida(id_partida);

        Juego juego = juegoDao.getJuegoByPartidaId(partida.getId());
        partida.setJuego(juego);

        List<Participante> participantes = participanteDao.getParticipantesByPartidaId(partida.getId());
        partida.set

        return partida;
    }
}
