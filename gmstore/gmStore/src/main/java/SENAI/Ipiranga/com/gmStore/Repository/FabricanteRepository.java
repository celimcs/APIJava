package SENAI.Ipiranga.com.gmStore.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SENAI.Ipiranga.com.gmStore.Model.Fabricante;

@Repository
public interface FabricanteRepository extends JpaRepository<Fabricante, Long> {
	public List<Fabricante> findByNomeContainingIgnoreCase(String nome);
	public List<Fabricante> findById(long id);

}










