package br.com.kumabe.controllers

import br.com.kumabe.dtos.ContatoDTO
import br.com.kumabe.models.Contato
import br.com.kumabe.repositories.ContatoRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDate
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ContatoControllerTests {
    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var contatoRepository: ContatoRepository

    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun `deve criar um novo contato`() {
        val fulano = Contato(1L, "Fulano", "(11) 99999-9999", LocalDate.of(1985, 3, 6))
        val fulanoDTO = ContatoDTO(1L, "Fulano", "(11) 99999-9999", LocalDate.of(1985, 3, 6))
        Mockito.`when`(contatoRepository.saveAndFlush(fulano)).thenReturn(fulano)
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fulanoDTO))
        ).andExpect(status().isCreated)
    }

    @Test
    fun `deve retornar um contato pelo código`() {
        val fulano = Contato(1L, "Fulano", "(11) 99999-9999", LocalDate.of(1985, 3, 6))
        Mockito.`when`(contatoRepository.findById(1L)).thenReturn(Optional.of(fulano))
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contatos/1"))
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("\$.codigo").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.nome").value("Fulano"))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.telefone").value("(11) 99999-9999"))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.data_nascimento").value("1985-03-06"))
                .andDo(print())
    }

    @Test
    fun `deve retornar um erro ao tentar procurar pelo código`() {
        Mockito.`when`(contatoRepository.findById(1L)).thenReturn(Optional.empty())
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contatos/1"))
                .andExpect(status().isNotFound)
                .andDo(print())
    }

    @Test
    fun `deve atualizar um contato`() {
        val fulano = Contato(1L, "Fulano", "(11) 99999-9999", LocalDate.of(1985, 3, 6))
        val fulanoDTO = ContatoDTO(1L, "Fulano", "(11) 99999-9999", LocalDate.of(1985, 3, 6))
        Mockito.`when`(contatoRepository.findById(1)).thenReturn(Optional.of(fulano))
        Mockito.`when`(contatoRepository.saveAndFlush(fulano)).thenReturn(fulano)
        Assertions.assertEquals(fulano.codigo, fulanoDTO.codigo)
    }
    @Test
    fun `deve excluir um contato pelo código`() {
        val fulano = Contato(1L, "Fulano", "(11) 99999-9999", LocalDate.of(1985, 3, 6))
        Mockito.`when`(contatoRepository.findById(1L)).thenReturn(Optional.of(fulano))
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/contatos/1"))
                .andExpect(status().isNoContent)
                .andDo(print())
    }

    @Test
    fun `deve retornar uma lista de contatos`() {
        val fulano = Contato(1L, "Fulano", "(11) 99999-9999", LocalDate.of(1985, 3, 6))
        val beltrano = Contato(2L, "Beltrano", "(21) 99999-9999", LocalDate.of(1985, 3, 6))
        val sicrano = Contato(3L, "Sicrano", "(37) 99999-9999", LocalDate.of(1985, 3, 6))
        val contatos = listOf(fulano, beltrano, sicrano)
        Mockito.`when`(contatoRepository.findAll()).thenReturn(contatos)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contatos"))
                .andExpect(status().isOk)
                .andDo(print())
    }

}