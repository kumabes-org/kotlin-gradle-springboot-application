package br.com.kumabe.controllers


import br.com.kumabe.dtos.ContatoDTO
import br.com.kumabe.models.Contato
import br.com.kumabe.repositories.ContatoRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.lang.Exception
import java.time.LocalDate
import java.util.Optional
import java.util.stream.Collector
import java.util.stream.Collectors
import javax.validation.Valid


@RestController
@RequestMapping("/api")
class ContatoController(private val contatoRepository: ContatoRepository) {
    private val LOGGER: Logger = LoggerFactory.getLogger(ContatoController::class.java)

    @PostMapping("/v1/contatos")
    fun create(@RequestBody @Valid contatoDTO: ContatoDTO, uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<Void> {
        val contato = dto2Entity(contatoDTO)
        val fromDatabase = contatoRepository.saveAndFlush(contato)
        val location = uriComponentsBuilder.path("/api/v1/contatos/{codigo}").buildAndExpand(fromDatabase.codigo).toUri()
        return ResponseEntity.created(location).build()
    }

    private fun dto2Entity(contatoDTO: ContatoDTO): Contato {
        return Contato(contatoDTO.codigo, contatoDTO.nome, contatoDTO.telefone, contatoDTO.dataNascimento)
    }

    private fun entity2DTO(contato: Contato): ContatoDTO {
        return ContatoDTO(contato.codigo, contato.nome, contato.telefone, contato.dataNascimento)
    }

    @GetMapping("/v1/contatos/{codigo}")
    fun retrieve(@PathVariable("codigo") codigo: Long): ResponseEntity<ContatoDTO> {
        LOGGER.info("Código: {}", codigo)
        val optional: Optional<Contato> = contatoRepository.findById(codigo)
        if (optional.isPresent) {
            val contatoDTO = entity2DTO(optional.get())
            return ResponseEntity.ok(contatoDTO)
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/v1/contatos/{codigo}")
    fun update(@PathVariable("codigo") codigo: Long, @RequestBody @Valid contatoDTO: ContatoDTO): ResponseEntity<ContatoDTO> {
        LOGGER.info("Código: {}", codigo)
        val optional: Optional<Contato> = contatoRepository.findById(codigo)
        if (optional.isPresent) {
            if(optional.get().codigo != codigo)
                return ResponseEntity.badRequest().build()
            val salvar = Contato(codigo, contatoDTO.nome, contatoDTO.telefone, contatoDTO.dataNascimento)
            contatoRepository.saveAndFlush(salvar)
            return ResponseEntity.ok(entity2DTO(salvar))
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/v1/contatos/{codigo}")
    fun delete(@PathVariable("codigo") codigo: Long): ResponseEntity<Void> {
        LOGGER.info("Código: {}", codigo)
        val optional: Optional<Contato> = contatoRepository.findById(codigo)
        if (optional.isPresent) {
            contatoRepository.delete(optional.get())
            return ResponseEntity.noContent().build()
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/v1/contatos")
    fun list(): ResponseEntity<List<ContatoDTO>> {
        val contatos = contatoRepository.findAll().stream().map { c ->
            entity2DTO(c)
        }.collect(Collectors.toList())
        return ResponseEntity.ok(contatos)
    }

}