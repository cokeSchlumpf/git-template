package ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

import constants.CloudantConfiguration;
import constants.GitHubConfiguration;
import model.Template;
import model.TemplatePutResponse;
import model.TemplateRequest;
import model.TemplateVariable;
import ws.exceptions.InternalServerError;
import ws.exceptions.NoTemplateConfigurationException;
import ws.exceptions.RepositoryNotFoundException;

@Path("templates")
@Singleton
public class TemplatesService {

	private static final Template DEFAULT_TEMPLATE = new Template("cokeSchlumpf", "think-hack-together", //
			"refs/head/master", "/", "UTF-8", new String[] { "*.js", "package.json" },
			new TemplateVariable("foo", "bar"), new TemplateVariable("egon", "olsen"));

	private static final Logger logger = Logger.getLogger(TemplatesService.class.getName());

	{
		logger.log(Level.INFO, CloudantConfiguration.getConfiguration());
	}

	@Context
	private UriInfo uri;

	@GET
	@Path("{uuid}")
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response clone(@PathParam("uuid") String uuid) {
		Template template = withDb(db -> db.find(Template.class, uuid));

		logger.log(Level.INFO, "Download " + template);

		StreamingOutput out = new StreamingOutput() {

			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				ZipOutputStream zipOut = new ZipOutputStream(os);
				zipContent(zipOut, template);
				zipOut.flush();
				zipOut.close();
			}
		};

		ResponseBuilder response = Response.ok(out);
		response.type(MediaType.APPLICATION_OCTET_STREAM);
		response.header("Content-Disposition", "attachment; filename=\"" + template.getRepository() + ".zip\"");
		return response.build();
	}

	@GET
	@Path("example")
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	public Template example() {
		return DEFAULT_TEMPLATE;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TemplatePutResponse put(Template template) {
		return withDb(db -> {
			String id = db.save(template).getId();
			logger.log(Level.INFO, "Stored " + template);
			return new TemplatePutResponse(id, uri.getAbsolutePath().toString() + "/" + id);
		});
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String receiveTemplate(TemplateRequest templateRequest) {
		GHRepository rep = null;
		
		try {
			rep = GitHub.connectUsingPassword(GitHubConfiguration.USERNAME, GitHubConfiguration.PASSWORD) //
					.getUser(templateRequest.getUsername())
					.getRepository(templateRequest.getRepository());
		} catch (IOException e) {
			throw new InternalServerError(e);
		} finally {
			if (rep == null) {
				throw new RepositoryNotFoundException(templateRequest.getUsername(), templateRequest.getRepository());
			}
		}

		try {
			return rep.getDirectoryContent("/")//
					.stream()//
					.filter(content -> content.getName().equals(".template")) //
					.findFirst() //
					.map(file -> {
						try {
							return this.readString(file.read(), "UTF-8");
						} catch (Exception e) {
							logger.log(Level.SEVERE, e.getMessage(), e);
							throw new InternalServerError(e);
						}
					}) //
					.orElseThrow(() -> new NoTemplateConfigurationException(templateRequest.getUsername(),
							templateRequest.getRepository()));
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new InternalServerError(e);
		}
	}

	private void copy(InputStream in, OutputStream out) throws IOException {
		IOUtils.copy(in, out);
	}

	private void filter(Template template, InputStream in, OutputStream out) throws IOException {
		String content = readString(in, template.getEncoding());

		for (TemplateVariable variable : template.getVariables()) {
			content = content.replace("__" + variable.getName() + "__", variable.getValue());
		}

		out.write(content.getBytes(Charset.forName(template.getEncoding())));
	}

	private boolean filterFile(Template template, String filename) {
		for (String pattern : template.getFiles()) {
			if (pattern.startsWith("*") && filename.endsWith(pattern.substring(1))) {
				return true;
			} else if (!pattern.startsWith("/") && filename.endsWith("/" + pattern)) {
				return true;
			} else if (filename.equals(template.getRepository() + "/" + filename)) {
				return true;
			}
		}

		return false;
	}

	private String readString(InputStream in, String encoding) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer, encoding);
		return writer.toString();
	}

	private <T> T withDb(Function<Database, T> func) {
		CloudantClient client = new CloudantClient(CloudantConfiguration.USERNAME, CloudantConfiguration.USERNAME,
				CloudantConfiguration.PASSWORD);
		Database db = client.database(CloudantConfiguration.DATABASE, true);
		return func.apply(db);
	}

	private void zipContent(ZipOutputStream out, Template template) {
		try {
			GHRepository rep = GitHub//
					.connectUsingPassword(GitHubConfiguration.USERNAME, GitHubConfiguration.PASSWORD) //
					.getUser(template.getUsername()) //
					.getRepository(template.getRepository());

			String commit = template.getCommit();

			if (commit != null && commit.length() > 0) {
				zipContent(out, template, rep, rep.getDirectoryContent(template.getRootPath(), commit));
			} else {
				zipContent(out, template, rep, rep.getDirectoryContent(template.getRootPath()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void zipContent(ZipOutputStream out, Template template, GHRepository repositoy, List<GHContent> content) {
		content.stream().forEach(file -> {
			String name = file.getPath();

			try {
				if (file.isFile()) {
					out.putNextEntry(new ZipEntry(name));
					InputStream in = file.read();
					if (filterFile(template, name)) {
						logger.log(Level.INFO, "Filter           " + name);
						filter(template, in, out);
					} else {
						logger.log(Level.INFO, "Copy             " + name);
						copy(in, out);
					}
					in.close();
				} else {
					logger.log(Level.INFO, "Create directory " + name);
					out.putNextEntry(new ZipEntry(name + "/"));
				}

				out.closeEntry();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if (file.isDirectory()) {
				try {
					zipContent(out, template, repositoy, repositoy.getDirectoryContent(file.getPath()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
