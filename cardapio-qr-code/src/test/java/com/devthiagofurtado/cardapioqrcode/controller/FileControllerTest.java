package com.devthiagofurtado.cardapioqrcode.controller;

import com.devthiagofurtado.cardapioqrcode.config.FileStorageConfig;
import com.devthiagofurtado.cardapioqrcode.data.vo.CardapioVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.UsuarioVO;
import com.devthiagofurtado.cardapioqrcode.modelCreator.CardapioModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.UserModelCreator;
import com.devthiagofurtado.cardapioqrcode.security.jwt.JwtTokenProvider;
import com.devthiagofurtado.cardapioqrcode.service.CardapioService;
import com.devthiagofurtado.cardapioqrcode.service.FileStorageService;
import com.devthiagofurtado.cardapioqrcode.service.JasperService;
import com.devthiagofurtado.cardapioqrcode.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.Principal;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private FileController fileController;

    @Mock
    private UserService userService;

    @Mock
    private JasperService jasperService;

    @Mock
    private CardapioService cardapioService;

    private Path fileStorageLocation;

    @Mock
    FileStorageService fileStorageService = new FileStorageService(new FileStorageConfig("C:\\Users\\thiag\\Documents\\Meu Projeto\\upload"));

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper mapper;

    private final String BASE_URL = "/api/file/v1";

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private HttpHeaders headers;

    @BeforeEach
    void setUp() throws MalformedURLException {
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Authorization", "blablabla");

        var user = UserModelCreator.vo(UserModelCreator.permissionVOS(PermissionVO.ADMIN), true, true);

        CardapioVO cardapioVO = CardapioModelCreator.vo(1L, true, 2L);

        BDDMockito.when(userService.findById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString()))
                .thenReturn(user);

        BDDMockito.when(userService.findAllByUserName(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class), ArgumentMatchers.anyString()))
                .thenReturn(new PageImpl<>(Collections.singletonList(user)));

        BDDMockito.when(userService.salvar(ArgumentMatchers.any(UsuarioVO.class), ArgumentMatchers.anyString()))
                .thenReturn(user);

        BDDMockito.when(userService.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true));

        BDDMockito.when(userService.loadUserByUsername(ArgumentMatchers.anyString()))
                .thenReturn(UserModelCreator.userDetails(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true)));

        BDDMockito.when(jwtTokenProvider.getUsername(ArgumentMatchers.anyString()))
                .thenReturn("teste");

        BDDMockito.when(cardapioService.salvar(ArgumentMatchers.any(CardapioVO.class), ArgumentMatchers.anyString()))
                .thenReturn(cardapioVO);

        BDDMockito.when(jasperService.exportarPDF())
                .thenReturn(new byte[123]);

        BDDMockito.when(fileStorageService.storeFile(ArgumentMatchers.any(MultipartFile.class)))
                .thenReturn("Teste.pdf");

        BDDMockito.when(fileStorageService.loadFileAsResource(ArgumentMatchers.anyString()))
                .thenReturn(new UrlResource("file:///C:/Users/thiag/Documents/Meu%20Projeto/upload/test2.txt/"));

    }


    @Test
    void uploadFile() throws Exception {

        headers = new HttpHeaders();
        headers.add("Content-Type", "multipart/form-data");
        headers.add("Authorization", "blablabla");

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());

        LinkedMultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        FileInputStream fis = new FileInputStream("C:\\Users\\thiag\\Desktop\\ads.pdf");

        mockMvc.perform( MockMvcRequestBuilders.multipart(BASE_URL+"/uploadFile")
                        .file(firstFile).headers(headers))
                .andExpect(status().isOk());

    }

    @Test
    void downloadFile() throws Exception {
        HttpServletRequest request = new HttpServletRequest() {
            @Override
            public String getAuthType() {
                return null;
            }

            @Override
            public Cookie[] getCookies() {
                return new Cookie[0];
            }

            @Override
            public long getDateHeader(String name) {
                return 0;
            }

            @Override
            public String getHeader(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return null;
            }

            @Override
            public int getIntHeader(String name) {
                return 0;
            }

            @Override
            public String getMethod() {
                return null;
            }

            @Override
            public String getPathInfo() {
                return null;
            }

            @Override
            public String getPathTranslated() {
                return null;
            }

            @Override
            public String getContextPath() {
                return null;
            }

            @Override
            public String getQueryString() {
                return null;
            }

            @Override
            public String getRemoteUser() {
                return null;
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getRequestedSessionId() {
                return null;
            }

            @Override
            public String getRequestURI() {
                return null;
            }

            @Override
            public StringBuffer getRequestURL() {
                return null;
            }

            @Override
            public String getServletPath() {
                return null;
            }

            @Override
            public HttpSession getSession(boolean create) {
                return null;
            }

            @Override
            public HttpSession getSession() {
                return null;
            }

            @Override
            public String changeSessionId() {
                return null;
            }

            @Override
            public boolean isRequestedSessionIdValid() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromCookie() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromURL() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromUrl() {
                return false;
            }

            @Override
            public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
                return false;
            }

            @Override
            public void login(String username, String password) throws ServletException {

            }

            @Override
            public void logout() throws ServletException {

            }

            @Override
            public Collection<Part> getParts() throws IOException, ServletException {
                return null;
            }

            @Override
            public Part getPart(String name) throws IOException, ServletException {
                return null;
            }

            @Override
            public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass) throws IOException, ServletException {
                return null;
            }

            @Override
            public Object getAttribute(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

            }

            @Override
            public int getContentLength() {
                return 0;
            }

            @Override
            public long getContentLengthLong() {
                return 0;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletInputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public String getParameter(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getParameterNames() {
                return null;
            }

            @Override
            public String[] getParameterValues(String name) {
                return new String[0];
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return null;
            }

            @Override
            public String getProtocol() {
                return null;
            }

            @Override
            public String getScheme() {
                return null;
            }

            @Override
            public String getServerName() {
                return null;
            }

            @Override
            public int getServerPort() {
                return 0;
            }

            @Override
            public BufferedReader getReader() throws IOException {
                return null;
            }

            @Override
            public String getRemoteAddr() {
                return null;
            }

            @Override
            public String getRemoteHost() {
                return null;
            }

            @Override
            public void setAttribute(String name, Object o) {

            }

            @Override
            public void removeAttribute(String name) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public Enumeration<Locale> getLocales() {
                return null;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public RequestDispatcher getRequestDispatcher(String path) {
                return null;
            }

            @Override
            public String getRealPath(String path) {
                return null;
            }

            @Override
            public int getRemotePort() {
                return 0;
            }

            @Override
            public String getLocalName() {
                return null;
            }

            @Override
            public String getLocalAddr() {
                return null;
            }

            @Override
            public int getLocalPort() {
                return 0;
            }

            @Override
            public ServletContext getServletContext() {
                return new ServletContext() {
                    @Override
                    public String getContextPath() {
                        return null;
                    }

                    @Override
                    public ServletContext getContext(String uripath) {
                        return null;
                    }

                    @Override
                    public int getMajorVersion() {
                        return 0;
                    }

                    @Override
                    public int getMinorVersion() {
                        return 0;
                    }

                    @Override
                    public int getEffectiveMajorVersion() {
                        return 0;
                    }

                    @Override
                    public int getEffectiveMinorVersion() {
                        return 0;
                    }

                    @Override
                    public String getMimeType(String file) {
                        return "teste";
                    }

                    @Override
                    public Set<String> getResourcePaths(String path) {
                        return null;
                    }

                    @Override
                    public URL getResource(String path) throws MalformedURLException {
                        return null;
                    }

                    @Override
                    public InputStream getResourceAsStream(String path) {
                        return null;
                    }

                    @Override
                    public RequestDispatcher getRequestDispatcher(String path) {
                        return null;
                    }

                    @Override
                    public RequestDispatcher getNamedDispatcher(String name) {
                        return null;
                    }

                    @Override
                    public Servlet getServlet(String name) throws ServletException {
                        return null;
                    }

                    @Override
                    public Enumeration<Servlet> getServlets() {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getServletNames() {
                        return null;
                    }

                    @Override
                    public void log(String msg) {

                    }

                    @Override
                    public void log(Exception exception, String msg) {

                    }

                    @Override
                    public void log(String message, Throwable throwable) {

                    }

                    @Override
                    public String getRealPath(String path) {
                        return null;
                    }

                    @Override
                    public String getServerInfo() {
                        return null;
                    }

                    @Override
                    public String getInitParameter(String name) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getInitParameterNames() {
                        return null;
                    }

                    @Override
                    public boolean setInitParameter(String name, String value) {
                        return false;
                    }

                    @Override
                    public Object getAttribute(String name) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getAttributeNames() {
                        return null;
                    }

                    @Override
                    public void setAttribute(String name, Object object) {

                    }

                    @Override
                    public void removeAttribute(String name) {

                    }

                    @Override
                    public String getServletContextName() {
                        return null;
                    }

                    @Override
                    public ServletRegistration.Dynamic addServlet(String servletName, String className) {
                        return null;
                    }

                    @Override
                    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
                        return null;
                    }

                    @Override
                    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
                        return null;
                    }

                    @Override
                    public ServletRegistration.Dynamic addJspFile(String jspName, String jspFile) {
                        return null;
                    }

                    @Override
                    public <T extends Servlet> T createServlet(Class<T> c) throws ServletException {
                        return null;
                    }

                    @Override
                    public ServletRegistration getServletRegistration(String servletName) {
                        return null;
                    }

                    @Override
                    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
                        return null;
                    }

                    @Override
                    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
                        return null;
                    }

                    @Override
                    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
                        return null;
                    }

                    @Override
                    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
                        return null;
                    }

                    @Override
                    public <T extends Filter> T createFilter(Class<T> c) throws ServletException {
                        return null;
                    }

                    @Override
                    public FilterRegistration getFilterRegistration(String filterName) {
                        return null;
                    }

                    @Override
                    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
                        return null;
                    }

                    @Override
                    public SessionCookieConfig getSessionCookieConfig() {
                        return null;
                    }

                    @Override
                    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {

                    }

                    @Override
                    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
                        return null;
                    }

                    @Override
                    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
                        return null;
                    }

                    @Override
                    public void addListener(String className) {

                    }

                    @Override
                    public <T extends EventListener> void addListener(T t) {

                    }

                    @Override
                    public void addListener(Class<? extends EventListener> listenerClass) {

                    }

                    @Override
                    public <T extends EventListener> T createListener(Class<T> c) throws ServletException {
                        return null;
                    }

                    @Override
                    public JspConfigDescriptor getJspConfigDescriptor() {
                        return null;
                    }

                    @Override
                    public ClassLoader getClassLoader() {
                        return null;
                    }

                    @Override
                    public void declareRoles(String... roleNames) {

                    }

                    @Override
                    public String getVirtualServerName() {
                        return null;
                    }

                    @Override
                    public int getSessionTimeout() {
                        return 0;
                    }

                    @Override
                    public void setSessionTimeout(int sessionTimeout) {

                    }

                    @Override
                    public String getRequestCharacterEncoding() {
                        return null;
                    }

                    @Override
                    public void setRequestCharacterEncoding(String encoding) {

                    }

                    @Override
                    public String getResponseCharacterEncoding() {
                        return null;
                    }

                    @Override
                    public void setResponseCharacterEncoding(String encoding) {

                    }
                };
            }

            @Override
            public AsyncContext startAsync() throws IllegalStateException {
                return null;
            }

            @Override
            public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                return null;
            }

            @Override
            public boolean isAsyncStarted() {
                return false;
            }

            @Override
            public boolean isAsyncSupported() {
                return false;
            }

            @Override
            public AsyncContext getAsyncContext() {
                return null;
            }

            @Override
            public DispatcherType getDispatcherType() {
                return null;
            }
        };

        mockMvc.perform(get(BASE_URL + "/downloadFile/test2.txt").headers(headers)).andExpect(status().isOk());
    }

    @Test
    void uploadFiles() throws Exception {
        headers = new HttpHeaders();
        headers.add("Content-Type", "multipart/form-data");
        headers.add("Authorization", "blablabla");

        MockMultipartFile firstFile = new MockMultipartFile("files", "filename.txt", "text/plain", "some xml".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("files", "filename2.txt", "text/plain", "some xml".getBytes());

        LinkedMultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        FileInputStream fis = new FileInputStream("C:\\Users\\thiag\\Desktop\\ads.pdf");

        mockMvc.perform( MockMvcRequestBuilders.multipart(BASE_URL+"/uploadMultipleFiles")
                        .file(firstFile)
                        .file(secondFile)
                        .headers(headers))
                .andExpect(status().isOk());
    }
}