package repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import daos.VendaDAO;
import dtos.ItemVendaDetalheDTO;
import dtos.VendaDTO;
import models.Venda;

public class VendaRepository {
    private final VendaDAO dao = new VendaDAO();

    public void salvarVendaCompleta(Venda venda) throws SQLException {
        dao.salvarVendaCompleta(venda);
    }

    public List<VendaDTO> listarVendas() throws SQLException {
        return dao.listarVendas();
    }

    public List<ItemVendaDetalheDTO> listarItensPorVenda(UUID idVenda) throws SQLException {
        return dao.listarItensPorVenda(idVenda);
    }
}
