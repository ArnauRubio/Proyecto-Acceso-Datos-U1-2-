package org.egibide.repositories;

import org.egibide.dao.AsociacionDao;
import org.egibide.dao.JuegoDao;
import org.egibide.dao.PartidaDao;
import org.egibide.dao.SociaDao;
import org.egibide.idao.AsociacionDaoImpl;
import org.egibide.idao.JuegoDaoImpl;
import org.egibide.idao.PartidaDaoImpl;
import org.egibide.idao.SociaDaoImpl;
import org.egibide.irepositories.AsociacionRepository;
import org.egibide.irepositories.JuegoRepository;
import org.egibide.models.Asociacion;
import org.egibide.models.Juego;
import org.egibide.models.Partida;
import org.egibide.models.Socia;

import java.util.List;

public class JuegoRepositoryImpl implements JuegoRepository {

    private AsociacionDao asociacionDao = new AsociacionDaoImpl();
    private JuegoDao juegoDao = new JuegoDaoImpl();
    private PartidaDao partidaDao = new PartidaDaoImpl();

    @Override
    public Juego getJuego(int id_juego) {
        Juego juego = juegoDao.getJuego(id_juego);

        Asociacion asociacion = asociacionDao.getAsociacionByJuegoId(juego.getId());
        juego.setAsociacion(asociacion);

        List<Partida> partidas = partidaDao.getPartidasByJuegoId(juego.getId());
        juego.setPartidas(partidas);

        return juego;
    }
}
