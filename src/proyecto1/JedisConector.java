package proyecto1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import redis.clients.jedis.Jedis;

public class JedisConector {

    private final Jedis jedis;

    public JedisConector() {
        // Crear una conexión a un servidor Redis
        jedis = new Jedis("redis-19086.c256.us-east-1-2.ec2.redns.redis-cloud.com", 19086);
        jedis.auth("Ssc2Sld7FVY2wfXosDhwnq6eeRN2VBK0"); // Autenticación si es necesaria
    }

    public String[] iniciarSesion(String usuario, String clave) {
        String id = jedis.hget("index:nombre:usuario", usuario);
        if (id != null) {
            String contraAlmacenada = jedis.hget("usuario:" + id, "contra");
            if (clave.equals(contraAlmacenada)) {
                String[] temp = new String[3];
                temp[0] = id;
                temp[1] = jedis.hget("usuario:" + id, "idVinculado");
                temp[2] = jedis.hget("usuario:" + id, "tipoUsuario");
                return temp; // Inicio de sesión exitoso
            } else {
                return new String[]{"Contraseña incorrecta"}; // Contraseña incorrecta
            }
        } else {
            return new String[]{"Usuario no encontrado"}; // Usuario no encontrado
        }
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
        datosTrabajo.put("puesto", puesto);
        datosTrabajo.put("salario", salario);
        datosTrabajo.put("tipoEmpleo", tipoEmpleo);
        datosTrabajo.put("funcion", funcion);
        datosTrabajo.put("tituloRequisito", tituloRequisito);
        datosTrabajo.put("nivelEstudioMinimo", nivelEstudioMinimo);
        datosTrabajo.put("experiencia", experiencia);
        datosTrabajo.put("cupos", cupos);
        datosTrabajo.put("idEmpresa", idEmpresa);

        // Almacenar los datos del trabajo en un hash
        jedis.hmset("trabajo:" + id, datosTrabajo);
        jedis.sadd("index:empresa:trabajo:" + idEmpresa, id);
    }

    //Eliminar
    public void eliminarTrabajo(String idTrabajo, String idEmpresa) {

        // Eliminar la asociación del trabajo con la empresa
        jedis.srem("index:empresa:trabajo:" + idEmpresa, idTrabajo);

        Set<String> idSolicitantes = jedis.smembers("index:trabajo:solicitantes:" + idTrabajo);

        // Para cada solicitante, eliminar la solicitud asociada
        for (String idPersona : idSolicitantes) {
            jedis.srem("index:solicitud:trabajo:" + idPersona, idTrabajo);
        }

        // Obtener los IDs de los trabajadores aceptados para el trabajo
        Set<String> idsEmpleadosAceptados = jedis.smembers("trabajo:trabajadores:" + idTrabajo);

        // Eliminar las relaciones entre el trabajo y los trabajadores aceptados
        for (String idEmpleado : idsEmpleadosAceptados) {
            jedis.srem("trabajo:trabajadores:" + idTrabajo, idEmpleado);
        }

        // Eliminar las solicitudes asociadas al trabajo
        Set<String> idsSolicitudes = jedis.smembers("index:trabajo:solicitantes:" + idTrabajo);
        for (String idSolicitud : idsSolicitudes) {
            jedis.srem("index:trabajo:solicitantes:" + idTrabajo, idSolicitud);
            jedis.del("solicitud:" + idSolicitud);
        }

        // Finalmente, eliminar el trabajo
        jedis.del("trabajo:" + idTrabajo);
        jedis.del("index:trabajo:solicitantes:" + idTrabajo);
    }

    public void eliminarUsuario(String id) {
        jedis.hdel("index:nombre:usuario", jedis.hget("usuario:" + id, "nombre"));
        jedis.del("usuario:" + id);
    }

    public void eliminarEmpresa(String id) {
        eliminarUsuario(jedis.hget("empresa:" + id, "idUsuario"));
        jedis.hdel("index:nombre:empresa", jedis.hget("empresa:" + id, "nombre"));
        jedis.del("empresa:" + id);
    }

    public void eliminarPersona(String id) {
        eliminarUsuario(jedis.hget("persona:" + id, "idUsuario"));
        jedis.hdel("index:nombre:persona", jedis.hget("persona:" + id, "nombre"));
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

    public void modificarTrabajo(String id, String campo, String nuevoValor) {
        jedis.hset("trabajo:" + id, campo, nuevoValor);
    }

    //EL listar es un poco mas complicado lo dejo al final
    public Set<String> listarPersonas() {

        Set<String> keys = jedis.keys("persona:*");
        Set<String> nombresEmpresas = new HashSet<>();
        for (String key : keys) {
            Map<String, String> datosEmpresa = jedis.hgetAll(key);
            String nombreEmpresa = datosEmpresa.get("nombre");
            String idEmpresa = key.substring(8); // Obtener solo el ID eliminando el prefijo "Empresa:"
            nombresEmpresas.add(nombreEmpresa + " (ID: " + idEmpresa + ")");
        }
        return nombresEmpresas;
    }

    public Set<String> listarTrabajos() {
        Set<String> keys = jedis.keys("trabajo:*");
        Set<String> nombresEmpresas = new HashSet<>();
        for (String key : keys) {
            Map<String, String> datosEmpresa = jedis.hgetAll(key);
            String nombreEmpresa = datosEmpresa.get("puesto");
            String idEmpresa = key.substring(8); // Obtener solo el ID eliminando el prefijo "Empresa:"
            nombresEmpresas.add(nombreEmpresa + " (ID: " + idEmpresa + ")");
        }
        return nombresEmpresas;
    }

    public Set<String> listarAplicantesDeUnTrabajo(String id) {
        Set<String> idsTrabajos = jedis.smembers("index:trabajo:solicitantes:" + id);

        System.out.println("Metodo no listo");
//        // Crear un conjunto para almacenar los nombres de los trabajos
//        Set<String> nombresTrabajos = new HashSet<>();
//
//        // Iterar sobre los IDs de los trabajos y obtener sus detalles
//        for (String idTrabajo : idsTrabajos) {
//            String key = "trabajo:" + idTrabajo;
//            Map<String, String> datosTrabajo = jedis.hgetAll(key);
//            String nombreTrabajo = datosTrabajo.get("puesto");
//            nombresTrabajos.add(nombreTrabajo + " (ID: " + idTrabajo + ")  "+"a");
//        }
        return null;
        //return nombresTrabajos;
    }

    public Set<String> listarTrabajos(String id) {
        Set<String> idsTrabajos = jedis.smembers("index:empresa:trabajo:" + id);

        // Crear un conjunto para almacenar los nombres de los trabajos
        Set<String> nombresTrabajos = new HashSet<>();

        // Iterar sobre los IDs de los trabajos y obtener sus detalles
        for (String idTrabajo : idsTrabajos) {
            String key = "trabajo:" + idTrabajo;
            Map<String, String> datosTrabajo = jedis.hgetAll(key);
            String nombreTrabajo = datosTrabajo.get("puesto");
            nombresTrabajos.add(nombreTrabajo + " (ID: " + idTrabajo + ")");
        }

        return nombresTrabajos;
    }

    public String obtenerAtributoDeTrabajo(String id, String atributo) {
        String key = "trabajo:" + id;
        return jedis.hget(key, atributo);
    }

    public String obtenerAtributoEmpleado(String id, String atributo) {
        String key = "persona:" + id;
        return jedis.hget(key, atributo);
    }

    public String obtenerAtributodeidEmpresa(String id, String atributo) {
        String key = "empresa:" + id;
        return jedis.hget(key, atributo);
    }

    public DefaultListModel<String> listarTrabajosJList(String idEmpresa) {
        Set<String> idsTrabajos = jedis.smembers("index:empresa:trabajo:" + idEmpresa);

        DefaultListModel<String> model = new DefaultListModel<>();

        for (String idTrabajo : idsTrabajos) {
            String key = "trabajo:" + idTrabajo;
            Map<String, String> datosTrabajo = jedis.hgetAll(key);
            String nombreTrabajo = datosTrabajo.get("puesto");
            String cupos = datosTrabajo.get("cupos");
            model.addElement("Puesto: " + nombreTrabajo + "    Cupos: " + cupos + " (ID: " + idTrabajo + ")");
        }
        return model;
    }

    public DefaultListModel<String> listarTrabajosJList() {
        Set<String> keys = jedis.keys("trabajo:*");

        DefaultListModel<String> model = new DefaultListModel<>();

        for (String idTrabajo : keys) {
            String key = idTrabajo;
            Map<String, String> datosTrabajo = jedis.hgetAll(key);
            String nombreTrabajo = datosTrabajo.get("puesto");
            String cupos = datosTrabajo.get("cupos");
            String idNUmerito = key.substring(8);
            model.addElement("Puesto: " + nombreTrabajo + "    Cupos: " + cupos + " (ID: " + idNUmerito + ")");
        }
        return model;
    }

    public DefaultListModel<String> listarTrabajosDisponibleJList() {
        Set<String> keys = jedis.keys("trabajo:*");

        DefaultListModel<String> model = new DefaultListModel<>();

        for (String idTrabajo : keys) {
            String key = idTrabajo;
            Map<String, String> datosTrabajo = jedis.hgetAll(key);
            int int_Cupos = Integer.valueOf(datosTrabajo.get("cupos"));
            if (int_Cupos > 0) {
                String nombreTrabajo = datosTrabajo.get("puesto");
                String cupos = datosTrabajo.get("cupos");
                String idNUmerito = key.substring(8);
                model.addElement("Puesto: " + nombreTrabajo + "    Cupos: " + cupos + " (ID: " + idNUmerito + ")");
            }
        }
        return model;
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

    public String extraerId(String texto) {
        String[] partes = texto.split("\\(ID: ");
        if (partes.length >= 2) {
            String idEmpresa = partes[1].replace(")", "");
            return idEmpresa;
        }
        return null; // Devuelve null si no se pudo extraer el ID
    }
//    public String extraerId(String texto) {
//        String[] partes = texto.split("\\(ID:\\s*");
//        if (partes.length >= 2) {
//            String idEmpresa = partes[1].replace(")", "").trim(); // Trim para eliminar espacios alrededor del ID
//            return idEmpresa;
//        }
//        return null; // Devuelve null si no se pudo extraer el ID
//    }

    public Set<String> listarUsuario() {
        Set<String> keys = jedis.keys("usuario:*");
        Set<String> nombresEmpresas = new HashSet<>();
        for (String key : keys) {
            Map<String, String> datosEmpresa = jedis.hgetAll(key);
            String nombreEmpresa = datosEmpresa.get("nombre");
            String idEmpresa = key.substring(8); // Obtener solo el ID eliminando el prefijo "Empresa:"
            nombresEmpresas.add(nombreEmpresa + " (ID: " + idEmpresa + ")");
        }
        return nombresEmpresas;
    }

    public void agregarSolicitante(String idTrabajo, String idPersona) {
        jedis.sadd("index:trabajo:solicitantes:" + idTrabajo, idPersona);
        jedis.sadd("index:solicitud:trabajo:" + idPersona, idTrabajo);

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

    public DefaultListModel<String> mostrarAplicantesTrabajoJList(String id) {
        Set<String> idEmpleados = jedis.smembers("index:trabajo:solicitantes:" + id);
        DefaultListModel<String> model = new DefaultListModel<>();

        for (String idEmpleado : idEmpleados) {
            String nombre = jedis.hget("persona:" + idEmpleado, "nombre");
            model.addElement(nombre + " (ID: " + idEmpleado + ")");
        }

        return model;
    }

    public DefaultTableModel mostrarAplicantesTrabajo(String id) {
        Set<String> idEmpleados = jedis.smembers("index:trabajo:solicitantes:" + id);
        Set<String> keys = new HashSet<>();

        for (String idEmpleado : idEmpleados) {
            keys.add("persona:" + idEmpleado);
        }

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
        Set<String> keys = jedis.keys("trabajo:*");

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
                datosTrabajo.get("puesto"),
                datosTrabajo.get("salario"),
                datosTrabajo.get("tipoEmpleo"),
                datosTrabajo.get("funcion"),
                datosTrabajo.get("tituloRequisito"),
                datosTrabajo.get("nivelEstudioMinimo"),
                datosTrabajo.get("experiencia"),
                datosTrabajo.get("cupos"),
                datosTrabajo.get("idEmpresa")
            });
        }

        return model;
    }

    public DefaultTableModel BuscarEmpresa(String id) {
        Set<String> keys = jedis.keys("empresa:" + id);

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

    public DefaultTableModel BuscarPersona(String id) {
        Set<String> keys = new HashSet<>();

        keys.add("persona:" + id);

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

    public DefaultTableModel mostrarTrabajosSolicitados(String idSolicitante) {
        // Obtener los IDs de los trabajos solicitados por el solicitante
        Set<String> idsTrabajos = jedis.smembers("index:solicitud:trabajo:" + idSolicitante);

        // Crear un modelo de tabla con las columnas necesarias
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Puesto");
        model.addColumn("Salario");
        model.addColumn("Tipo de Empleo");
        model.addColumn("Función");
        model.addColumn("Nivel de Estudio Mínimo");
        model.addColumn("Experiencia");

        // Para cada trabajo solicitado, obtener los datos y agregarlos al modelo
        for (String idTrabajo : idsTrabajos) {
            Map<String, String> datosTrabajo = jedis.hgetAll("trabajo:" + idTrabajo);
            model.addRow(new Object[]{
                idTrabajo,
                datosTrabajo.get("puesto"),
                datosTrabajo.get("salario"),
                datosTrabajo.get("tipoEmpleo"),
                datosTrabajo.get("funcion"),
                datosTrabajo.get("nivelEstudioMinimo"),
                datosTrabajo.get("experiencia")
            });
        }

        return model;
    }

    public DefaultTableModel mostrarTrabajosAceptados(String idSolicitante) {
        // Obtener los IDs de los trabajos aceptados por el solicitante
        Set<String> idsTrabajosAceptados = jedis.smembers("index:trabajo:aceptados:" + idSolicitante);

        // Crear un modelo de tabla con las columnas necesarias
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Puesto");
        model.addColumn("Salario");
        model.addColumn("Tipo de Empleo");
        model.addColumn("Función");
        model.addColumn("Nivel de Estudio Mínimo");
        model.addColumn("Experiencia");

        // Para cada trabajo aceptado, obtener los datos y agregarlos al modelo
        for (String idTrabajo : idsTrabajosAceptados) {
            Map<String, String> datosTrabajo = jedis.hgetAll("trabajo:" + idTrabajo);
            model.addRow(new Object[]{
                idTrabajo,
                datosTrabajo.get("puesto"),
                datosTrabajo.get("salario"),
                datosTrabajo.get("tipoEmpleo"),
                datosTrabajo.get("funcion"),
                datosTrabajo.get("nivelEstudioMinimo"),
                datosTrabajo.get("experiencia")
            });
        }

        return model;
    }

    public DefaultTableModel mostrarEmpleadosAceptados(String idTrabajo) {
        // Obtener los IDs de los empleados aceptados para el trabajo
        Set<String> idsEmpleadosAceptados = jedis.smembers("trabajo:trabajadores:" + idTrabajo);

        // Crear un modelo de tabla con las columnas necesarias
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Correo");
        model.addColumn("Teléfono");

        // Para cada empleado aceptado, obtener los datos y agregarlos al modelo
        for (String idEmpleado : idsEmpleadosAceptados) {
            Map<String, String> datosEmpleado = jedis.hgetAll("persona:" + idEmpleado);
            model.addRow(new Object[]{
                idEmpleado,
                datosEmpleado.get("nombre"),
                datosEmpleado.get("correo"),
                datosEmpleado.get("telefono")
            });
        }

        return model;
    }

    public void AceptarSolicitud(String idSolicitante, String idTrabajo) {
        // Eliminar la solicitud del solicitante para este trabajo
        jedis.srem("index:solicitud:trabajo:" + idSolicitante, idTrabajo);

        // Eliminar al solicitante de la lista de solicitantes para este trabajo
        jedis.srem("index:trabajo:solicitantes:" + idTrabajo, idSolicitante);

        // Agregar al solicitante como trabajador en el trabajo
        jedis.sadd("index:trabajo:trabajadores:" + idTrabajo, idSolicitante);

        // Agregar el trabajo a la lista de trabajos aceptados para el solicitante
        jedis.sadd("index:trabajo:aceptados:" + idSolicitante, idTrabajo);

        jedis.hset("trabajo:" + idTrabajo, "cupos", String.valueOf(Integer.parseInt(jedis.hget("trabajo:" + idTrabajo, "cupos")) - 1));
    }

    public void close() {
        jedis.close();
    }
}
