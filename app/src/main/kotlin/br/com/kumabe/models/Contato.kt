package br.com.kumabe.models

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "contatos")
data class Contato(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "codigo")
        val codigo: Long,
        @Column(name = "nome")
        val nome: String,
        @Column(name = "telefone")
        val telefone: String,
        @Column(name = "data_nascimento")
        val dataNascimento: LocalDate)