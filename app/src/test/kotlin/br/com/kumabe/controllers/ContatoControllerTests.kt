package br.com.kumabe.controllers

import br.com.kumabe.dtos.ContatoDTO
import br.com.kumabe.models.Contato
import br.com.kumabe.repositories.ContatoRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate
import java.util.*

class ContatoControllerTest {
    private val contatoRepository = Mockito.mock(ContatoRepository::class.java)
    private val contatoController = ContatoController(contatoRepository)

    @Test
    fun `should create a new contato`() {
        val contatoDTO = ContatoDTO(1L, "John Doe", "123456789", LocalDate.now())
        val contato = Contato(1L, "John Doe", "123456789", LocalDate.now())
        Mockito.`when`(contatoRepository.saveAndFlush(contato)).thenReturn(contato)
        val uriComponentsBuilder = UriComponentsBuilder.newInstance()
        val responseEntity: ResponseEntity<Void> = contatoController.create(contatoDTO, uriComponentsBuilder)
        assertNotNull(responseEntity)
        assertEquals(HttpStatus.CREATED, responseEntity.statusCode)
    }

    @Test
    fun `should retrieve a contato by codigo`() {
        val codigo = 1L
        val contato = Contato(codigo, "John Doe", "123456789", LocalDate.now())
        Mockito.`when`(contatoRepository.findById(codigo)).thenReturn(Optional.of(contato))
        val responseEntity: ResponseEntity<ContatoDTO> = contatoController.retrieve(codigo)
        assertNotNull(responseEntity)
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertTrue(responseEntity.hasBody())
        assertEquals(contato.codigo, responseEntity.body?.codigo)
        assertEquals(contato.nome, responseEntity.body?.nome)
        assertEquals(contato.telefone, responseEntity.body?.telefone)
        assertEquals(contato.dataNascimento, responseEntity.body?.dataNascimento)
    }

    @Test
    fun `should retrieve a contato by codigo and not found`() {
        val codigo = 1L
        val contato = Contato(codigo, "John Doe", "123456789", LocalDate.now())
        Mockito.`when`(contatoRepository.findById(codigo)).thenReturn(Optional.ofNullable(null))
        val responseEntity: ResponseEntity<ContatoDTO> = contatoController.retrieve(codigo)
        System.out.println(responseEntity)
        assertNotNull(responseEntity)        
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.statusCode)
    }

    @Test
    fun `should update a contato by codigo`() {
        val codigo = 1L
        val contatoDTO = ContatoDTO(codigo, "John Doe", "123456789", LocalDate.now())
        val contato = Contato(codigo, "Jane Doe", "987654321", LocalDate.now())
        Mockito.`when`(contatoRepository.findById(codigo)).thenReturn(Optional.of(contato))
        Mockito.`when`(contatoRepository.saveAndFlush(contato)).thenReturn(contato)
        val responseEntity: ResponseEntity<Void> = contatoController.update(codigo, contatoDTO)
        assertNotNull(responseEntity)
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
    }

    @Test
    fun `should delete a contato by codigo`() {
        val codigo = 1L
        val contato = Contato(codigo, "John Doe", "123456789", LocalDate.now())
        Mockito.`when`(contatoRepository.findById(codigo)).thenReturn(Optional.of(contato))
        val responseEntity: ResponseEntity<Void> = contatoController.delete(codigo)
        assertNotNull(responseEntity)
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.statusCode)
    }

    @Test
    fun `should list all contatos`() {
        val contatos = listOf(
            Contato(1L, "John Doe", "123456789", LocalDate.now()),
            Contato(2L, "Jane Doe", "987654321", LocalDate.now())
        )
        Mockito.`when`(contatoRepository.findAll()).thenReturn(contatos)
        val responseEntity: ResponseEntity<List<ContatoDTO>> = contatoController.list()
        assertNotNull(responseEntity)
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertTrue(responseEntity.hasBody())
        assertEquals(contatos.size, responseEntity.body?.size)
        assertEquals(contatos[0].codigo, responseEntity.body?.get(0)?.codigo)
        assertEquals(contatos[0].nome, responseEntity.body?.get(0)?.nome)
        assertEquals(contatos[0].telefone, responseEntity.body?.get(0)?.telefone)
        assertEquals(contatos[0].dataNascimento, responseEntity.body?.get(0)?.dataNascimento)
        assertEquals(contatos[1].codigo, responseEntity.body?.get(1)?.codigo)
        assertEquals(contatos[1].nome, responseEntity.body?.get(1)?.nome)
        assertEquals(contatos[1].telefone, responseEntity.body?.get(1)?.telefone)
        assertEquals(contatos[1].dataNascimento, responseEntity.body?.get(1)?.dataNascimento)
    }
}