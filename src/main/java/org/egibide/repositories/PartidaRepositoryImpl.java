package org.egibide.repositories;

import org.egibide.dao.*;
import org.egibide.idao.*;
import org.egibide.irepositories.PartidaRepository;
import org.egibide.irepositories.SociaRepository;
import org.egibide.models.*;

import java.util.List;

/**
 * Implementación del repositorio de partidas y carga de sus relaciones.
 */
public class PartidaRepositoryImpl implements PartidaRepository {

    private JuegoDao juegoDao = new JuegoDaoImpl();
    private PartidaDao partidaDao = new PartidaDaoImpl();
    private ParticipanteDao participanteDao = new ParticipanteDaoImpl();

    /**
     * Recupera la partida, el juego asociado y las listas de participantes y ganadoras.
     */
    @Override
    public Partida getPartida(int id_partida) {
        Partida partida = partidaDao.getPartida(id_partida);

        Juego juego = juegoDao.getJuegoByPartidaId(partida.getId());
        partida.setJuego(juego);

        List<Participante> participantes = participanteDao.getParticipantesByPartidaId(partida.getId());
        partida.setJugadoras(participantes);

        List<Participante> ganadoras = participanteDao.getGanadorasByPartidaId(partida.getId());
        partida.setGanadoras(ganadoras);

        return partida;
    }
}
