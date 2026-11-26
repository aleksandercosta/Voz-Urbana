# VozUrbana — Aula 15 / AV2

Esta versão foi construída a partir do arquivo `VozUrbana.7z` fornecido nesta conversa.
**Atenção:** esse arquivo ainda não tinha as classes DTO/Projection e as autorizações por proprietário da Aula 14.
Para atender os critérios finais da AV2, essas alterações também foram incorporadas.

## Iniciar

1. Abra esta pasta no VS Code (a que contém `pom.xml`). Use Java 21.
2. Inicie o MySQL e configure `DB_USERNAME` (opcional; padrão `root`), `DB_PASSWORD` (opcional se o banco não tiver senha) e `JWT_SECRET` no ambiente. Use uma chave JWT aleatória com pelo menos 32 bytes.
3. Execute `./mvnw spring-boot:run` no Linux/Mac ou `.\mvnw.cmd spring-boot:run` no PowerShell.
4. No VS Code, abra a pasta `/view` com **Live Server** em `http://127.0.0.1:5500` ou `http://localhost:5500`.
5. Abra `index.html`, entre com um usuário que possua hash BCrypt ou faça um cadastro novo em `signup.html`.
6. Após login, `dashboard.html` consulta `/usuario/me` e `/denuncia/user` com `Authorization: Bearer <JWT>`.
7. Sem token ou com token inválido, o dashboard redireciona para o login. Logout remove o token do `localStorage`.

## Endpoints essenciais

| Método | Rota | Permissão | Retorno |
|---|---|---|---|
| POST | `/usuario` | Pública | 201 + `UsuarioResponseDTO` |
| POST | `/login` | Pública | 200 + JWT em header e body |
| GET | `/usuario/me` | Autenticado | Dados básicos sem CPF, senha ou perfis |
| GET | `/usuario/user` | Autenticado | Projection leve do próprio usuário |
| GET | `/usuario/{id}` | Dono ou ADMIN | DTO sem campos sensíveis |
| GET | `/usuario` | ADMIN | Lista de Projections |
| PUT/DELETE | `/usuario/{id}` | Dono ou ADMIN | Atualização/exclusão |
| POST | `/relator` | Autenticado | Novo relator vinculado ao usuário logado |
| POST | `/denuncia` | Dono do relator ou ADMIN | 201 + DTO |
| GET | `/denuncia/user` | Autenticado | Projection de denúncias do usuário |
| GET | `/denuncia/{id}` | Dono ou ADMIN | 200 DTO ou 403 |
| PUT/DELETE | `/denuncia/{id}` | Dono ou ADMIN | CRUD protegido |

A relação usada para filtrar a denúncia é `Denuncia → Relator → Usuario`.
O `Usuario.profiles` está mapeado para a coluna `id_usuario` da tabela `user_profiles`,
pois no HeidiSQL desta conversa essa coluna já existia e era obrigatória.
Se seu banco tiver estrutura diferente, confira `SHOW CREATE TABLE user_profiles` antes de alterar qualquer coluna.
Cadastro de Relator continua usando o modelo atual da sua API; o backend ignora qualquer `usuario` enviado e associa automaticamente o usuário autenticado.
Acesso ao campo `status` em PUT `/denuncia/{id}` é exclusivo de ADMIN.

## Postman — fluxo automático

1. Importe `postman/VozUrbana_AV2_Collection.postman_collection.json`.
2. Variáveis da collection: `host=http://localhost:8080`, `authorization_token` (começa vazia), `usuario_email`, `usuario_senha`, `usuario_cpf`.
3. Execute as pastas na ordem **01 Auth → 02 Usuários → 03 Relator → 04 Denúncias** (uma vez).
4. Cadastro salva `usuario_id`; login lê o header/body e salva `authorization_token`; relator salva `relator_id`; denúncia salva `denuncia_id`.
5. Todas as rotas protegidas herdam `Bearer {{authorization_token}}` automaticamente. Não precisa copiar o token.
6. Para repetir o cadastro, altere `usuario_email` e `usuario_cpf` para **outros dados fictícios** (são únicos no banco).
7. Os testes da pasta **05 Segurança** são separados: para `outro_usuario_id` informe ID real de outro usuário.
8. Para testar ADMIN, configure um ADMIN real no banco (perfil `ADMIN` ligado corretamente ao ID), informe `admin_email`/`admin_senha`, faça o login ADMIN, depois GET para `outro_usuario_id`. Não associe ADMIN via cadastro público.
9. Faça o **DELETE da denúncia por último**, pois exclui o recurso criado durante a demonstração.

## Criar dados para aparecer no dashboard

Depois de cadastrar e fazer login pelo Postman, execute `POST /relator` com seu token e corpo:

```json
{"nome":"Aluno AV2","cpf":"98765432104","cidade":"Palhoça","estado":"SC"}
```

Pegue `idRelator` retornado e execute `POST /denuncia` com o token:

```json
{"idRelator":1,"titulo":"Buraco na rua","descricao":"Buraco no centro"}
```

Troque o ID 1 pelo `idRelator` real. Ao abrir o dashboard com esse usuário, a denúncia aparecerá.

## Checklist de entrega

- [ ] Login pelo frontend, JWT salvo e dashboard carregado
- [ ] Frontend sem token redireciona; logout apaga token
- [ ] Collection Postman exportada e scripts salvando o JWT automaticamente
- [ ] GET do dono = 200; GET de terceiro = 403; GET ADMIN = 200
- [ ] Respostas sem senha, CPF ou profiles
- [ ] ZIP com projeto + `/view` + collection + README
- [ ] Vídeo (6–8 min) ou apresentação em sala, conforme o enunciado

## Segurança

Este frontend armazena JWT no `localStorage` **porque é requisito da atividade**. Em produção, avalie os riscos de XSS,
políticas de conteúdo, cookies HttpOnly e proteção contra outras ameaças.
Nunca publique `application.properties` contendo sua senha real do banco ou uma chave JWT real. Mova credenciais/chaves
para variáveis de ambiente antes de versionar ou divulgar o código; use apenas contas fictícias nas demonstrações.

## Validação da entrega

Os arquivos foram gerados e suas referências verificadas estaticamente. **Não foram executados testes com MySQL/Postman nem compilação Maven completa neste ambiente**.
Confirme localmente com o VS Code e o Postman antes da entrega. Se algum endpoint falhar, anote o erro de console.
