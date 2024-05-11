package proyecto1;

import java.util.Map;
import java.util.Set;
import redis.clients.jedis.Jedis;

public class JedisConector {

    private final Jedis jedis;

    public JedisConector() {
        // Crear una conexión a un servidor Redis
        jedis = new Jedis("redis-19086.c256.us-east-1-2.ec2.redns.redis-cloud.com", 19086);
        jedis.auth("Ssc2Sld7FVY2wfXosDhwnq6eeRN2VBK0"); // Autenticación si es necesaria
    }

    // Métodos para interactuar con Redis
    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public String get(String key) {
        return jedis.get(key);
    }

    public void CrearPersona(String Nombre, String identidad, String Correo, String Residencia, String Nacimiento, String Telefono, String Nacionalidad, String EstadoCivil, String NumeroEmergencia, String CorreoEmergencia, String Idiomas, String Graduacion, String Titulo, String EstudiosAfines, String Certificaciones, String NivelDeEstudio, String Experiencia, String NumeroReferencia, String CorreoReferencia, String TipoDeEmpleado, String SalarioEsperado, String NombreReferencia) {
        String id = jedis.get("CrearIdPersona"); // Obtener el ID actual
        if (id == null) {
            jedis.set("CrearIdPersona", "0"); // Inicializar el contador si es null
            id = "0";
        } else {
            jedis.incr("CrearIdPersona"); // Incrementar el contador para el próximo ID
            id = jedis.get("CrearIdPersona");
        }

        jedis.hset("persona:" + id, "nombre", Nombre);
        jedis.hset("persona:" + id, "identidad", identidad);
        jedis.hset("persona:" + id, "correo", Correo);
        jedis.hset("persona:" + id, "residencia", Residencia);
        jedis.hset("persona:" + id, "nacimiento", Nacimiento);
        jedis.hset("persona:" + id, "telefono", Telefono);
        jedis.hset("persona:" + id, "nacionalidad", Nacionalidad);
        jedis.hset("persona:" + id, "estadoCivil", EstadoCivil);
        jedis.hset("persona:" + id, "numeroEmergencia", NumeroEmergencia);
        jedis.hset("persona:" + id, "correoEmergencia", CorreoEmergencia);
        jedis.hset("persona:" + id, "idiomas", Idiomas);
        jedis.hset("persona:" + id, "graduacion", Graduacion);
        jedis.hset("persona:" + id, "titulo", Titulo);
        jedis.hset("persona:" + id, "estudiosAfines", EstudiosAfines);
        jedis.hset("persona:" + id, "certificaciones", Certificaciones);
        jedis.hset("persona:" + id, "nivelEstudio", NivelDeEstudio);
        jedis.hset("persona:" + id, "experiencia", Experiencia);
        jedis.hset("persona:" + id, "numeroReferencia", NumeroReferencia);
        jedis.hset("persona:" + id, "correoReferencia", CorreoReferencia);
        jedis.hset("persona:" + id, "tipoEmpleado", TipoDeEmpleado);
        jedis.hset("persona:" + id, "salarioEsperado", SalarioEsperado);
        jedis.hset("persona:" + id, "nombreReferencia", NombreReferencia);

        jedis.hset("persona:index:nombre", Nombre, id);

    }

    public void CrearEmpresa(String Nombre, String CIF, String Director, String Direccion, String Rubro, String Sector, String Fecha_Fundacion, String Nacion_Origen) {
        String id = jedis.get("CrearIdEmpresa"); // Obtener el ID actual
        if (id == null) {
            jedis.set("CrearIdEmpresa", "0"); // Inicializar el contador si es null
            id = "0";
        } else {
            jedis.incr("CrearIdEmpresa"); // Incrementar el contador para el próximo ID
            id = jedis.get("CrearIdEmpresa");
        }

        jedis.hset("empresa:" + id, "nombre", Nombre);
        jedis.hset("empresa:" + id, "CIF", CIF);
        jedis.hset("empresa:" + id, "director", Director);
        jedis.hset("empresa:" + id, "direccion", Direccion);
        jedis.hset("empresa:" + id, "rubro", Rubro);
        jedis.hset("empresa:" + id, "sector", Sector);
        jedis.hset("empresa:" + id, "fechaFundacion", Fecha_Fundacion);
        jedis.hset("empresa:" + id, "nacionOrigen", Nacion_Origen);

        jedis.hset("empresa:index:nombre", Nombre, id);
    }

    public void CrearUsuario(String nombreUsuario, String contra, String tipoUsuario) {
        String id = jedis.get("CrearIdUsuario");
        if (id == null) {
            jedis.set("CrearIdUsuario", "0");
            id = "0";
        } else {
            jedis.incr("CrearIdUsuario");
            id = jedis.get("CrearIdUsuario");
        }

        jedis.hset("usuario:" + id, "nombre", nombreUsuario);
        jedis.hset("usuario:" + id, "contra", contra);
        jedis.hset("usuario:" + id, "tipoUsuario", tipoUsuario);
        jedis.hset("usuario:index:nombre", nombreUsuario, id);
    }

    public void eliminarUsuario(String id) {
        jedis.del("usuario:" + id);
        jedis.hdel("usuario:index:nombre", jedis.hget("usuario:" + id, "nombre"));
    }

    public void eliminarEmpresa(String id) {
        jedis.del("empresa:" + id);
        jedis.hdel("empresa:index:nombre", jedis.hget("empresa:" + id, "nombre"));
    }

    public void eliminarPersona(String id) {
        jedis.del("persona:" + id);
        jedis.hdel("persona:index:nombre", jedis.hget("persona:" + id, "nombre"));
    }

    public void modificarPersona(String id, String campo, String nuevoValor) {
        if (campo.equals("nombre")) {
            jedis.hdel("persona:index:nombre", jedis.hget("persona:" + id, "nombre"));
            jedis.hset("persona:" + id, campo, nuevoValor);
            jedis.hset("persona:index:nombre", campo, id);
        } else {
            jedis.hset("persona:" + id, campo, nuevoValor);
        }
    }

    public void modificarEmpresa(String id, String campo, String nuevoValor) {
        if (campo.equals("nombre")) {
            jedis.hdel("empresa:index:nombre", jedis.hget("empresa:" + id, "nombre"));
            jedis.hset("empresa:" + id, campo, nuevoValor);
            jedis.hset("empresa:index:nombre", campo, id);
        } else {
            jedis.hset("empresa:" + id, campo, nuevoValor);
        }
    }

    public void modificarUsuario(String id, String campo, String nuevoValor) {
        if (campo.equals("nombre")) {
            jedis.hdel("usuario:index:nombre", jedis.hget("usuario:" + id, "nombre"));
            jedis.hset("usuario:" + id, campo, nuevoValor);
            jedis.hset("usuario:index:nombre", campo, id);
        } else {
            jedis.hset("usuario:" + id, campo, nuevoValor);
        }
    }

    //EL listar es un poco mas complicado lo dejo al final
    public void listarPersonas() {
        Set<String> keys = jedis.keys("persona:*");
        for (String key : keys) {
            Map<String, String> datosPersona = jedis.hgetAll(key);
            System.out.println("Persona ID: " + key.substring(8)); // Elimina el prefijo "persona:" para mostrar solo el ID
            for (Map.Entry<String, String> entry : datosPersona.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("-----------------------------------------");
        }
    }

    public void close() {
        jedis.close();
    }
}
