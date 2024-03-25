package org.scandura.tgid.controller;

import org.scandura.tgid.dto.TransacaoRequest;
import org.scandura.tgid.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TransacaoController {
    @Autowired
    private TransacaoService transacaoService;

    @PostMapping("/transacoes")
    public ResponseEntity<?> realizarTransacao(@RequestBody TransacaoRequest request) {
        ResponseEntity response;
        try {
            transacaoService.realizarTransacao(request.getCpfCliente(), request.getCnpjEmpresa(), request.getValor(), request.getTipoTransacao());
            response= ResponseEntity.ok("Transação realizada com sucesso");
            sendWebhook(request,response);

            return response;
        } catch (RuntimeException e) {
            response=ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            sendWebhook(request,response);

            return response;
        }
    }

    private void sendWebhook(TransacaoRequest request,ResponseEntity response ) {
        String webhookUrl = "https://webhook.site/d36ff7d0-4f47-4fbb-90ac-00a875fd8528";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForLocation(webhookUrl, request, response);
    }
}