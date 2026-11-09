package com.ouvidoria;

import com.ouvidoria.entity.*;
import com.ouvidoria.enums.Prioridade;
import com.ouvidoria.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

/*
 * =====================================================================
 * PROJETO: Ouvidoria Municipal - Sistema de Reclamacoes
 * Gerado a partir do diagrama de classes (UML) do trabalho de APS/APX
 * =====================================================================
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando o servidor REST - Ouvidoria Municipal");
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner initData(CidadaoRepository cidadaoRepo,
                                OrgaoResponsavelRepository orgaoRepo,
                                CategoriaProblemaRepository categoriaRepo,
                                TecnicoCampoRepository tecnicoRepo,
                                ServidorRepository servidorRepo,
                                ReclamacaoRepository reclamacaoRepo) {
        return args -> {
            // Cidadaos
            Cidadao c1 = new Cidadao();
            c1.setNome("Ana Pereira");
            c1.setCpf("111.222.333-44");
            c1.setEmail("ana.pereira@email.com");
            c1.setSenha("123456");
            c1.setTelefone("(11) 98888-1111");
            c1.setDataNascimento(LocalDate.of(1990, 5, 20));
            cidadaoRepo.save(c1);

            Cidadao c2 = new Cidadao();
            c2.setNome("Bruno Alves");
            c2.setCpf("555.666.777-88");
            c2.setEmail("bruno.alves@email.com");
            c2.setSenha("123456");
            c2.setTelefone("(11) 98888-2222");
            c2.setDataNascimento(LocalDate.of(1985, 11, 3));
            cidadaoRepo.save(c2);

            // Orgao responsavel
            OrgaoResponsavel orgao = new OrgaoResponsavel();
            orgao.setNome("Secretaria de Obras e Infraestrutura");
            orgao.setCnpj("12.345.678/0001-90");
            orgao.setTelefone("(11) 3333-4444");
            orgao.setEmail("obras@prefeitura.gov.br");
            orgaoRepo.save(orgao);

            // Categoria de problema
            CategoriaProblema categoria = new CategoriaProblema();
            categoria.setNome("Buraco na via");
            categoria.setDescricao("Buracos e afundamentos no asfalto");
            categoria.setPrazoSlaHoras(72);
            categoria.setPrioridadePadrao(Prioridade.MEDIA);
            categoria.definirOrgaoPadrao(orgao);
            categoriaRepo.save(categoria);

            // Tecnico de campo
            TecnicoCampo tecnico = new TecnicoCampo();
            tecnico.setNome("Carlos Souza");
            tecnico.setCpf("999.888.777-66");
            tecnico.setEmail("carlos.souza@prefeitura.gov.br");
            tecnico.setSenha("123456");
            tecnico.setTelefone("(11) 97777-5555");
            tecnico.setDataNascimento(LocalDate.of(1988, 2, 15));
            tecnico.setMatricula("TC-0001");
            tecnico.setCargo("Tecnico de Manutencao Viaria");
            tecnico.setDataAdmissao(LocalDate.of(2015, 3, 1));
            tecnico.setEspecialidade("Pavimentacao");
            tecnico.setVeiculo("Caminhonete XYZ-1234");
            tecnico.setOrgaoResponsavel(orgao);
            tecnicoRepo.save(tecnico);

            // Administrador (Servidor que NAO e TecnicoCampo = perfil ADMIN no login)
            Servidor admin = new Servidor();
            admin.setNome("Administrador do Sistema");
            admin.setCpf("000.000.000-00");
            admin.setEmail("admin@ouvidoria.gov.br");
            admin.setSenha("admin123");
            admin.setTelefone("(11) 90000-0000");
            admin.setDataNascimento(LocalDate.of(1980, 1, 1));
            admin.setMatricula("ADM-0001");
            admin.setCargo("Administrador");
            admin.setDataAdmissao(LocalDate.of(2010, 1, 1));
            admin.setOrgaoResponsavel(orgao);
            servidorRepo.save(admin);

            // Reclamacao de exemplo
            Endereco endereco = new Endereco();
            endereco.setLogradouro("Rua das Flores");
            endereco.setNumero("123");
            endereco.setBairro("Centro");
            endereco.setCep("01000-000");
            endereco.setLatitude(-23.5505);
            endereco.setLongitude(-46.6333);

            Reclamacao r = new Reclamacao();
            r.setTitulo("Buraco grande na Rua das Flores");
            r.setDescricao("Buraco profundo causando risco a motociclistas");
            r.setCidadao(c1);
            r.setCategoria(categoria);
            r.setOrgaoResponsavel(orgao);
            r.setEndereco(endereco);
            r.abrir();
            r.setPrazoLimite(categoria.calcularPrazo(r.getPrioridade()));
            reclamacaoRepo.save(r);

            System.out.println("Dados de exemplo carregados com sucesso!");
            System.out.println("Protocolo da reclamacao de exemplo: " + r.getProtocolo());
            System.out.println("-------------------------------------------------");
            System.out.println("Contas de teste para login:");
            System.out.println("  CIDADAO  -> ana.pereira@email.com / 123456");
            System.out.println("  TECNICO  -> carlos.souza@prefeitura.gov.br / 123456");
            System.out.println("  ADMIN    -> admin@ouvidoria.gov.br / admin123");
            System.out.println("-------------------------------------------------");
        };
    }
}
