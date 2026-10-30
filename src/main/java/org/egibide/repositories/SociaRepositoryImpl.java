package org.egibide.repositories;

import org.egibide.dao.AsociacionDao;
import org.egibide.dao.ParticipanteDao;
import org.egibide.dao.PartidaDao;
import org.egibide.dao.SociaDao;
import org.egibide.idao.AsociacionDaoImpl;
import org.egibide.idao.ParticipanteDaoImpl;
import org.egibide.idao.PartidaDaoImpl;
import org.egibide.idao.SociaDaoImpl;
import org.egibide.irepositories.SociaRepository;
import org.egibide.models.Asociacion;
import org.egibide.models.Participante;
import org.egibide.models.Partida;
import org.egibide.models.Socia;

import java.util.List;

/**
 * Implementación del repositorio de socias y carga de sus relaciones.
 */
public class SociaRepositoryImpl implements SociaRepository {

    private AsociacionDao asociacionDao = new AsociacionDaoImpl();
    private SociaDao sociaDao = new SociaDaoImpl();
    private ParticipanteDao participanteDao = new ParticipanteDaoImpl();

    /**
     * Recupera la socia junto con su asociación y sus participaciones.
     */
    @Override
    public Socia getSocia(int id_socia) {
        Socia socia = sociaDao.getSocia(id_socia);

        Asociacion asociacion = asociacionDao.getAsociacionBySociaId(socia.getId());
        socia.setAsociacion(asociacion);

        List<Participante> participaciones = participanteDao.getParticipacionesBySociaId(socia.getId());
        socia.setParticipaciones(participaciones);

        return socia;
    }
}
