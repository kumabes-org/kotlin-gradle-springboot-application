package br.com.kumabe.controllers

import br.com.kumabe.dtos.ContatoDTO
import br.com.kumabe.models.Contato
import br.com.kumabe.repositories.ContatoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.stream.Collectors
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ContatoController(
    private val contatoRepository: ContatoRepository
) {
    @PostMapping("/v1/contatos")
    fun create(
        @RequestBody @Valid contatoDTO: ContatoDTO,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<Void> {
        val contato: Contato = dto2Entity(contatoDTO)
        val fromDatabase = contatoRepository.saveAndFlush(contato)
        val location =
            uriComponentsBuilder.path("/api/v1/contatos/{codigo}").buildAndExpand(fromDatabase.codigo).toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping("/v1/contatos/{codigo}")
    fun retrieve(@PathVariable("codigo") codigo: Long): ResponseEntity<ContatoDTO> {
        val optional = contatoRepository.findById(codigo)
        if(optional.isPresent){
            val contatoDTO: ContatoDTO = entity2DTO(optional.get())
            return ResponseEntity.ok(contatoDTO)
        }else {
            return ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/v1/contatos/{codigo}")
    fun update(@PathVariable("codigo") codigo: Long, @RequestBody @Valid contatoDTO: ContatoDTO): ResponseEntity<Void> {
        val optional = contatoRepository.findById(codigo)
        if(optional.isPresent){
            val contato = Contato(codigo, contatoDTO.nome, contatoDTO.telefone, contatoDTO.dataNascimento)
            contatoRepository.saveAndFlush(contato)
            return ResponseEntity.ok().build()
        }else {
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/v1/contatos/{codigo}")
    fun delete(@PathVariable("codigo") codigo: Long): ResponseEntity<Void> {
        val optional = contatoRepository.findById(codigo)
        if(optional.isPresent){
            contatoRepository.delete(optional.get())
            return ResponseEntity.noContent().build()
        }else {
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

    fun dto2Entity(contatoDTO: ContatoDTO): Contato {
        return Contato(contatoDTO.codigo, contatoDTO.nome, contatoDTO.telefone, contatoDTO.dataNascimento)
    }

    fun entity2DTO(contato: Contato): ContatoDTO {
        return ContatoDTO(contato.codigo, contato.nome, contato.telefone, contato.dataNascimento)
    }

}