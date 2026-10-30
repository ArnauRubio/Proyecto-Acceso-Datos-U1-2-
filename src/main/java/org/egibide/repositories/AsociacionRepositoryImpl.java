package org.egibide.repositories;

import org.egibide.dao.AsociacionDao;
import org.egibide.dao.JuegoDao;
import org.egibide.dao.SociaDao;
import org.egibide.idao.AsociacionDaoImpl;
import org.egibide.idao.JuegoDaoImpl;
import org.egibide.idao.SociaDaoImpl;
import org.egibide.irepositories.AsociacionRepository;
import org.egibide.models.Asociacion;
import org.egibide.models.Juego;
import org.egibide.models.Socia;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Implementación del repositorio de asociaciones mediante los DAO existentes.
 */
public class AsociacionRepositoryImpl implements AsociacionRepository {

    private AsociacionDao asociacionDao = new AsociacionDaoImpl();
    private JuegoDao juegoDao = new JuegoDaoImpl();
    private SociaDao sociaDao = new SociaDaoImpl();

    /**
     * Recupera la asociación y carga las listas que devuelven los DAO de juegos y socias.
     */
    @Override
    public Asociacion getAsociacion(int id_asociacion) {
        Asociacion asociacion = asociacionDao.getAsociacion(id_asociacion);

        List<Juego> juegos = juegoDao.getJuegos();
        asociacion.setJuegos(juegos);

        List<Socia> socias = sociaDao.getSocias();
        asociacion.setSocias(socias);

        return asociacion;
    }
}
