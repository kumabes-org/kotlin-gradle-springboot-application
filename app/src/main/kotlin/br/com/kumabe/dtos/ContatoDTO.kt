package br.com.kumabe.dtos


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
class ContatoDTO(
        val codigo: Long,
        val nome: String,
        val telefone: String,
        @JsonProperty("data_nascimento")
        val dataNascimento: LocalDate
)