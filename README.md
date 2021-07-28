# 04_SpringSecurityExample
## Implementation of authorization code flow with oauth2 and spring security
En este ejemplo tenemos hemos implementado los 4 flujos principales de oauth2 con spring security, los probaremos de la siguiente manera:

### Grant type: Resource Owner Password
Este es el flujo más "inseguro" y por eso se menciona primero, se caracteriza en que el usuario le proporciona su usuario/contraseña 
a la aplicación cliente, y no los ingresa directamente en el servidor de autenticación. Esto solo debe producirse si el usuario
confía plenamente en el cliente que va a solicitar al servidor de autenticación acceso a su información.
```
curl -X POST \
'http://localhost:8080/oauth/token?grant_type=password&username=maiki&password=12345' \
-H 'Authorization: Basic bXljbGllbnQ6MTIzNDU2Nzg5MA=='
```
Como se puede ver en curl, que viene a ser la llamada que haria el cliente al servidor de autenticación, se pasa el usuario/contraseña 
que previamente el usuario le ha facilitado al cliente, además de ello va la cabecera Authorization con el clientId/secret del cliente 
en base64, para el ejemplo **myclient:1234567890**

Esa petición directamente nos devuelve:
```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6Im1haWtpIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTYyNDU0OTI3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJiODM2NTZkYS03YjViLTQxZGMtYmE0MS03OGZkODNlOTJjOWQiLCJjbGllbnRfaWQiOiJteWNsaWVudCJ9.VJ00LRvJF8RIxC5TZG2L0LKSCygHit77LbnK9qjyjQE",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6Im1haWtpIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6ImI4MzY1NmRhLTdiNWItNDFkYy1iYTQxLTc4ZmQ4M2U5MmM5ZCIsImV4cCI6MTYyNDU0OTI3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI3YjMxODIyMS1hYjkyLTQzZTAtYTRiNy0wNDRhOGY2MDkwZDgiLCJjbGllbnRfaWQiOiJteWNsaWVudCJ9.QefFGsbkcW0Ckh3ookXNW8aeFW1lUrQQVVNCK32xsd4",
    "expires_in": 86399,
    "scope": "read write",
    "jti": "b83656da-7b5b-41dc-ba41-78fd83e92c9d"
}
```
### Grant type: Implicit
Este flujo es similar al flujo de Authorization Code pero simplicado, es decir, el usuario es el que interactúa directamente con el 
servidor de autenticación, este no le da su usuario/contraseña al cliente, sino que el cliente lo redirige a una pagina del servidor 
de autorización para que los ingrese, la diferencia con respecto al otro flujo radica en que el token de autorización se devuelve 
al cliente directamente en la url de callback, es decir, NO se hace uso de un código de autorización. 
En el ejemplo veremos la redirección hacia el servidor de autorización que hace el cliente en el navegador, pasándole además 
una url de callback que el servidor de autorización llamará una vez se haya autenticado el usuario.
```
Ponemos en el navegador lo siguiente:
http://localhost:8080/oauth/authorize?response_type=token&client_id=myclient&scope=read&redirect_uri=http://localhost:8081/callback.html
```
Finalmente después que el el usuario se haya autenticado el servidor de autorización hace el callback:
```
http://localhost:8081/callback.html#
access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6Im1haWtpIiwic2NvcGUiOlsicmVhZCJdLCJleHAiOjE2MjIzMDExOTQsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQURNSU4iXSwianRpIjoiNzBiZDUwMGMtZThjNi00NzRhLThiM2UtZTc1NjE4ZGUyMWM4IiwiY2xpZW50X2lkIjoibXljbGllbnQifQ.YzOoJTkTcW3aW9K5hjREE-mLK9dmWvHnll7Omy2l-_w&
token_type=bearer&
expires_in=86372&
jti=70bd500c-e8c6-474a-8b3e-e75618de21c8
```

### Grant type: Client Credentials
En este flujo no hay una participación del un usuario que otorgue sus credenciales, en este caso el cliente (la aplicación) es
la que tiene que autenticarse y eso basta para que el servidor de autorización otorgue el token.
En el ejemplo veremos la petición del cliente al servidor de autorización:
```
curl -X POST \
'http://localhost:8080/oauth/token?grant_type=client_credentials' \
-H 'Authorization: Basic YWRtaW5fY2xpZW50OmFkbWlucGFzcw=='
```
Como se puede ver en ese caso se envía el base64(clientId:secret) en la cabecera Authorization con el grant_type=client_credentials
y como respuesta directamente obtenemos un access token.
```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE2MjQ2MzgyMjIsImF1dGhvcml0aWVzIjpbIlJFQURfT05MWV9DTElFTlQiXSwianRpIjoiYzg4MDMwMmYtZjEzNi00NmUyLWI1YmQtODQ5YWRmYTRmNzY5IiwiY2xpZW50X2lkIjoibXljbGllbnQifQ.UAaTIf7fAXNWxLmiECn2PEXirJ0zdpfMDf4WYuryF5U",
    "token_type": "bearer",
    "expires_in": 86399,
    "scope": "read write",
    "jti": "c880302f-f136-46e2-b5bd-849adfa4f769"
}
```

### Grant type: Authorization Code
Este es el flujo mas "complejo" de oauth2 porque separa el proceso de autenticación y el proceso de obtención del token 
y conecta ambos pasos a través del uso de un authorization code.
En el ejemplo veremos (al igual que en el flujo Implicit) la redirección hacia el servidor de autorización que hace el 
cliente en el navegador, pasándole además una url de callback que el servidor de autorización llamará una vez se 
haya autenticado el usuario.
```
http://localhost:8080/oauth/authorize?response_type=code&client_id=admin_client&redirect_uri=http://localhost:8081/callback.html
```
Luego de que el usuario se autentique nos redirigirá a una pagina para que el mismo pueda aprobar los scopes que esta solicitando 
el cliente, luego de hacerlo, finalmente los redirigirá a la redirect_url con el code como query param:
```
http://localhost:8081/callback.html?code=1R2cig
```
Luego de ello, tenemos que llamar al endpoint para la obtención de token:
```
curl -X POST \
'http://localhost:8080/oauth/token?grant_type=authorization_code&code=1R2cig&redirect_uri=http://localhost:8081/login.html' \
-H 'Authorization: Basic YWRtaW5fY2xpZW50OmFkbWlucGFzcw=='
```
Obteniendo como resultado final:
```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6Im1haWtpIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTYyNDY0MTUxMiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI0MzBhMTUxMS0yOTJkLTRlOTUtOGUyZS02NDcxYzk3NDlkOGYiLCJjbGllbnRfaWQiOiJteWNsaWVudCJ9.YopJkZMrAbxxpg4EL7icgMrSrGFhMBt4cB369W8rFKU",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6Im1haWtpIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6IjQzMGExNTExLTI5MmQtNGU5NS04ZTJlLTY0NzFjOTc0OWQ4ZiIsImV4cCI6MTYyNDY0MTUxMiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI3NGFlZmM3MC1lZDA4LTQ2ZDctYTk1Zi1lMmE0N2U5ZmM4ZjAiLCJjbGllbnRfaWQiOiJteWNsaWVudCJ9.2LnrgksfZP1fQhrhcTu30LdofMV8nA1T-URk4q3WPnM",
    "expires_in": 86399,
    "scope": "read write",
    "jti": "430a1511-292d-4e95-8e2e-6471c9749d8f"
}
```
### Insertar usuarios

Un ejemplo para insertar usuarios es el siguiente, pero como este endpoint si esta protegido es necesario generar un token antes:
```
curl -X POST \
http://localhost:8080/users \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsImV4ZWN1dGUiXSwiZXhwIjoxNjI2MzI4NzI3LCJhdXRob3JpdGllcyI6WyJST0xFX1JFQURfT05MWV9DTElFTlQiLCJST0xFX0FETUlOX0NMSUVOVCJdLCJqdGkiOiIzMmJhYjZjMi0zZGY4LTQ1NjctODM1Ny1kYWVjMTg5Y2FjZjIiLCJjbGllbnRfaWQiOiJhZG1pbl9jbGllbnQifQ._QJXEqq_-kjM9wa_bu1B-0IBhGaZZm51i5fMGZ4K0Hk' \
-H 'Content-Type: application/json' \
-d '{
    "username": "test_user",
    "password": "testpass",
    "passwordRepeated": "testpass"
}'
```

### Insertar clientes

Un ejemplo de como insertar nuevos clientes es el siguiente:
```
curl -X POST \
http://localhost:8080/clients \
-H 'Content-Type: application/json' \
-H 'Postman-Token: 038ba9c1-9a9e-473e-aaac-d1a5410c0ab8' \
-H 'cache-control: no-cache' \
-d '{
    "client_id": "test_client",
    "client_secret": "adminpass",
    "name": "Stored client",
    "description": "Stored client in mongoDB",
    "grant_types": [
        "AUTHORIZATION_CODE", "IMPLICIT", "PASSWORD", "REFRESH_TOKEN", "CLIENT_CREDENTIALS"
    ],
    "redirect_urls": ["http://localhost:8082/login.html"],
    "scopes": [
        "read", "write", "execute"
    ],
    "roles": ["READ_ONLY_CLIENT"]
}'
```

### Pendientes:
#### 1. Cual es la diferencia entre un bearer token y un JWT? 
   
Los tokens bearer esta relacionados a la "Bearer authentication" tambien llamada autenticacion por token, 
es un esquema de autenticacion HTTP que involucra a estos tokens. "Bearer authentication" se puede entender 
como "give access to the bearer of this token". El bearer token es un cadena encriptada, generada por el servidor
como respuesta a un login. Luego el cliente debe enviar este token en la cabecera Authorization cuando 
solicita un recurso protegido.
Este esquema fue creado por OAuth 2.0 en el RFC 6750, de forma similar a la Basic authentication, 
una Bearer authentication debe ser usada bajo el protocolo HTTPS (SSL).
JWT (Json Web Token) es un tipo de token, un estandar qué está dentro del documento RFC 7519 y que define una estructura,
en el mismo se define un mecanismo para poder propagar entre dos partes, y de forma segura, la identidad 
de un determinado usuario, además con una serie de claims o privilegios. Estos privilegios están 
codificados en objetos de tipo JSON, que se incrustan dentro del payload o cuerpo de un mensaje y que va 
firmado digitalmente. Más detalle https://openwebinars.net/blog/que-es-json-web-token-y-como-funciona/

#### 2. Que es la propiedad resourceIds del cliente?
   
Los resourcesIds son los IDs de los recursos a los que puede acceder un client, haciendo un simil con Rhino
nosotros tenemos un conjunto de servicios agrupados por apis, y varias apis puede formar parte de un tenant, entonces
los ids de estas apis vendrian a encajar conceptualmente con esto. Tambien podemos crear scopes y agruparlos a 
nivel de api, y asi conectar que scopes tiene habilitados (Los que puede solicitarle al cliente) un cliente 
para acceder a un determinado recurso o conjunto de recursos (api).

#### 3. Que pasa si, yo usuario, no acepto todos los scopes?
Si acepto al menos uno de los scopes que me ofrece que acepte, entonces se generará el token y en el payload
veremos que el atributo "scope" únicamente estarán los que haya aceptado, si en su defecto no acepto ninguno entonces
no nos devolverá un authorization code sino que nos devolverá la siguiente url:
```
http://localhost:8081/callback.html?error=access_denied&error_description=User%20denied%20access
```

Tambien a raíz de esta pregunta he descubierto que podemos proteger servicios a nivel de scopes así:
```
   http
       ...
       .authorizeRequests()
           .antMatchers(HttpMethod.GET, "/employees").access("#oauth2.hasScope('write')")
       ...
```

#### 4. Como cambiar el tipo de token que hay que pasarle a spring-security?

XXX

5. Cambiar el ttl del token
6. Implementar la revocación de tokens?
7. Personalizar el formulario de login
8. Customizar el formulario de scopes   
9. No tenemos un decision manager customizado, lo queremos? Necesitamos mas criterios de validación, no solo los roles.
10. No tenemos un token filter customizado, lo queremos? Necesitamos tener distintos tipos de tokens.