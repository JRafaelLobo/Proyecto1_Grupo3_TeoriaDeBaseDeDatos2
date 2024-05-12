package proyecto1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.table.DefaultTableModel;
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

    //Crear
    public void CrearPersona(String Nombre, String identidad, String Correo, String Residencia, String Nacimiento, String Telefono, String Nacionalidad, String EstadoCivil, String NumeroEmergencia, String CorreoEmergencia, String Idiomas, String Graduacion, String Titulo, String EstudiosAfines, String Certificaciones, String NivelDeEstudio, String Experiencia, String NumeroReferencia, String CorreoReferencia, String TipoDeEmpleado, String SalarioEsperado, String NombreReferencia, String usuario, String clave) {
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

        jedis.hset("index:nombre:persona", Nombre, id);

        CrearUsuario(usuario, clave, "persona", id);
    }

    public void CrearEmpresa(String Nombre, String CIF, String Director, String Direccion, String Rubro, String Sector, String Fecha_Fundacion, String Nacion_Origen, String usuario, String clave) {
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

        CrearUsuario(usuario, clave, "empresa", id);

        jedis.hset("index:nombre:empresa", Nombre, id);
    }

    public void CrearUsuario(String nombreUsuario, String contra, String tipoUsuario, String idVinculado) {
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
        jedis.hset("usuario:" + id, "idVinculado", idVinculado);
        if (tipoUsuario.equals("persona")) {
            jedis.hset("persona:" + idVinculado, "idUsuario", id);
        }
        if (tipoUsuario.equals("empresa")) {
            jedis.hset("empresa:" + idVinculado, "idUsuario", id);
        }

        jedis.hset("index:nombre:usuario", nombreUsuario, id);
    }

    public void crearTrabajo(String puesto, String salario, String tipoEmpleo, String funcion, String tituloRequisito, String nivelEstudioMinimo, String experiencia, String cupos, String idEmpresa) {
        // Generar un nuevo ID para el trabajo
        String id = jedis.get("CrearIdTrabajo");
        if (id == null) {
            jedis.set("CrearIdTrabajo", "0");
            id = "0";
        } else {
            jedis.incr("CrearIdTrabajo");
            id = jedis.get("CrearIdTrabajo");
        }

        // Crear un hash con los datos del trabajo
        Map<String, String> datosTrabajo = new HashMap<>();
        datosTrabajo.put("Puesto", puesto);
        datosTrabajo.put("Salario", salario);
        datosTrabajo.put("TipoEmpleo", tipoEmpleo);
        datosTrabajo.put("Funcion", funcion);
        datosTrabajo.put("TituloRequisito", tituloRequisito);
        datosTrabajo.put("NivelEstudioMinimo", nivelEstudioMinimo);
        datosTrabajo.put("Experiencia", experiencia);
        datosTrabajo.put("Cupos", cupos);
        datosTrabajo.put("IdEmpresa", idEmpresa);

        // Almacenar los datos del trabajo en un hash
        jedis.hmset("Trabajo:" + id, datosTrabajo);
        jedis.sadd("index:empresa:trabajo:" + idEmpresa, id);
    }

    //Eliminar
    public void eliminarTrabajo(String idTrabajo, String idEmpresa) {
        // Eliminar el trabajo
        jedis.del("Trabajo:" + idTrabajo);

        // Eliminar la asociación del trabajo con la empresa
        jedis.srem("indes:empresa:trabajo:" + idEmpresa, idTrabajo);
    }

    public void eliminarUsuario(String id) {
        eliminarPersona(jedis.hget("usuario:" + id, "idVinculado"));
        jedis.hdel("index:nombre:usuario", jedis.hget("usuario:" + id, "nombre"));
        jedis.del("usuario:" + id);
    }

    public void eliminarEmpresa(String id) {
        eliminarUsuario(jedis.hget("empresa:" + id, "idUsuario"));
        jedis.hdel("index:empresa:nombre", jedis.hget("empresa:" + id, "nombre"));
        jedis.del("empresa:" + id);
    }

    public void eliminarPersona(String id) {
        eliminarUsuario(jedis.hget("persona:" + id, "idUsuario"));
        jedis.hdel("persona:nombre:persona", jedis.hget("persona:" + id, "nombre"));
        jedis.del("persona:" + id);
    }

    //Modificar
    public void modificarPersona(String id, String campo, String nuevoValor) {
        if (campo.equals("nombre")) {
            jedis.hdel("index:nombre:persona", jedis.hget("persona:" + id, "nombre"));
            jedis.hset("persona:" + id, campo, nuevoValor);
            jedis.hset("index:nombre:persona", campo, id);
        } else {
            jedis.hset("persona:" + id, campo, nuevoValor);
        }
    }

    public void modificarEmpresa(String id, String campo, String nuevoValor) {
        if (campo.equals("nombre")) {
            jedis.hdel("index:nombre:empresa", jedis.hget("empresa:" + id, "nombre"));
            jedis.hset("empresa:" + id, campo, nuevoValor);
            jedis.hset("index:nombre:empresa", campo, id);
        } else {
            jedis.hset("empresa:" + id, campo, nuevoValor);
        }
    }

    public void modificarUsuario(String id, String campo, String nuevoValor) {
        if (campo.equals("nombre")) {
            jedis.hdel("index:nombre:usuario", jedis.hget("usuario:" + id, "nombre"));
            jedis.hset("usuario:" + id, campo, nuevoValor);
            jedis.hset("index:nombre:usuario", campo, id);
        } else {
            jedis.hset("usuario:" + id, campo, nuevoValor);
        }
    }

    //EL listar es un poco mas complicado lo dejo al final
    public Set<String> listarPersonas() {

        Set<String> keys = jedis.keys("persona:*");

//        for (String key : keys) {
//            Map<String, String> datosPersona = jedis.hgetAll(key);
//            System.out.println("Persona ID: " + key.substring(8)); // Elimina el prefijo "persona:" para mostrar solo el ID
//            for (Map.Entry<String, String> entry : datosPersona.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }
//            System.out.println("-----------------------------------------");
//        }
        return keys;
    }

    public Set<String> listarTrabajos() {
        Set<String> keys = jedis.keys("Trabajo:*");

//        for (String key : keys) {
//            Map<String, String> datosTrabajo = jedis.hgetAll(key);
//            System.out.println("ID del Trabajo: " + key.substring(8)); // Elimina el prefijo "Trabajo:" para mostrar solo el ID
//            for (Map.Entry<String, String> entry : datosTrabajo.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }
//            System.out.println("-----------------------------------------");
//        }
        return keys;
    }

    public Set<String> listarEmpresas() {
        Set<String> keys = jedis.keys("empresa:*");
        Set<String> nombresEmpresas = new HashSet<>();
        for (String key : keys) {
            Map<String, String> datosEmpresa = jedis.hgetAll(key);
            String nombreEmpresa = datosEmpresa.get("nombre");
            String idEmpresa = key.substring(8); // Obtener solo el ID eliminando el prefijo "Empresa:"
            nombresEmpresas.add(nombreEmpresa + " (ID: " + idEmpresa + ")");
        }
        return nombresEmpresas;
    }

    public String extraerIdEmpresa(String texto) {
        String[] partes = texto.split("\\(ID: ");
        if (partes.length >= 2) {
            String idEmpresa = partes[1].replace(")", "");
            return idEmpresa;
        }
        return null; // Devuelve null si no se pudo extraer el ID
    }

    public void agregarSolicitante(String idTrabajo, String idPersona) {
        jedis.sadd("index:trabajo:solicitantes:" + idTrabajo, idPersona);
    }

    public DefaultTableModel mostrarDetallesEmpresas() {
        Set<String> keys = jedis.keys("empresa:*");

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("CIF");
        model.addColumn("Director");
        model.addColumn("Dirección");
        model.addColumn("Rubro");
        model.addColumn("Sector");
        model.addColumn("Fecha de Fundación");
        model.addColumn("Nación de Origen");

        for (String key : keys) {
            Map<String, String> datosEmpresa = jedis.hgetAll(key);
            String idEmpresa = key.substring(8); // Obtener solo el ID eliminando el prefijo "Empresa:"
            String nombre = datosEmpresa.get("nombre");
            String cif = datosEmpresa.get("CIF");
            String director = datosEmpresa.get("director");
            String direccion = datosEmpresa.get("direccion");
            String rubro = datosEmpresa.get("rubro");
            String sector = datosEmpresa.get("sector");
            String fechaFundacion = datosEmpresa.get("fechaFundacion");
            String nacionOrigen = datosEmpresa.get("nacionOrigen");

            model.addRow(new Object[]{idEmpresa, nombre, cif, director, direccion, rubro, sector, fechaFundacion, nacionOrigen});
        }

        return model;
    }

    public DefaultTableModel mostrarDetallesPersonas() {
        Set<String> keys = jedis.keys("persona:*");

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Identidad");
        model.addColumn("Correo");
        model.addColumn("Residencia");
        model.addColumn("Nacimiento");
        model.addColumn("Teléfono");
        model.addColumn("Nacionalidad");
        model.addColumn("Estado Civil");
        model.addColumn("Número de Emergencia");
        model.addColumn("Correo de Emergencia");
        model.addColumn("Idiomas");
        model.addColumn("Graduación");
        model.addColumn("Título");
        model.addColumn("Estudios Afines");
        model.addColumn("Certificaciones");
        model.addColumn("Nivel de Estudio");
        model.addColumn("Experiencia");
        model.addColumn("Número de Referencia");
        model.addColumn("Correo de Referencia");
        model.addColumn("Tipo de Empleado");
        model.addColumn("Salario Esperado");
        model.addColumn("Nombre de Referencia");

        for (String key : keys) {
            Map<String, String> datosPersona = jedis.hgetAll(key);
            String idPersona = key.substring(8); // Obtener solo el ID eliminando el prefijo "persona:"

            model.addRow(new Object[]{
                idPersona,
                datosPersona.get("nombre"),
                datosPersona.get("identidad"),
                datosPersona.get("correo"),
                datosPersona.get("residencia"),
                datosPersona.get("nacimiento"),
                datosPersona.get("telefono"),
                datosPersona.get("nacionalidad"),
                datosPersona.get("estadoCivil"),
                datosPersona.get("numeroEmergencia"),
                datosPersona.get("correoEmergencia"),
                datosPersona.get("idiomas"),
                datosPersona.get("graduacion"),
                datosPersona.get("titulo"),
                datosPersona.get("estudiosAfines"),
                datosPersona.get("certificaciones"),
                datosPersona.get("nivelEstudio"),
                datosPersona.get("experiencia"),
                datosPersona.get("numeroReferencia"),
                datosPersona.get("correoReferencia"),
                datosPersona.get("tipoEmpleado"),
                datosPersona.get("salarioEsperado"),
                datosPersona.get("nombreReferencia")
            });
        }

        return model;
    }

    public DefaultTableModel mostrarDetallesTrabajos() {
        Set<String> keys = jedis.keys("Trabajo:*");

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Trabajo");
        model.addColumn("Puesto");
        model.addColumn("Salario");
        model.addColumn("Tipo de Empleo");
        model.addColumn("Función");
        model.addColumn("Título de Requisito");
        model.addColumn("Nivel de Estudio Mínimo");
        model.addColumn("Experiencia");
        model.addColumn("Cupos");
        model.addColumn("ID Empresa");

        for (String key : keys) {
            Map<String, String> datosTrabajo = jedis.hgetAll(key);
            String idTrabajo = key.substring(8); // Obtener solo el ID eliminando el prefijo "Trabajo:"

            model.addRow(new Object[]{
                idTrabajo,
                datosTrabajo.get("Puesto"),
                datosTrabajo.get("Salario"),
                datosTrabajo.get("TipoEmpleo"),
                datosTrabajo.get("Funcion"),
                datosTrabajo.get("TituloRequisito"),
                datosTrabajo.get("NivelEstudioMinimo"),
                datosTrabajo.get("Experiencia"),
                datosTrabajo.get("Cupos"),
                datosTrabajo.get("IdEmpresa")
            });
        }

        return model;
    }

    public void close() {
        jedis.close();
    }
}
