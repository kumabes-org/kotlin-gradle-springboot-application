package br.com.kumabe.repositories

import br.com.kumabe.models.Contato
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface ContatoRepository : JpaRepository<Contato, Long>