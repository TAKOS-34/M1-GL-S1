
package org.anonbnr.web_services.employeeservice.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.anonbnr.web_services.employeeservice.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetEmployeeResponse_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "getEmployeeResponse");
    private final static QName _AddEmployeeResponse_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "addEmployeeResponse");
    private final static QName _CountResponse_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "countResponse");
    private final static QName _Count_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "count");
    private final static QName _GetEmployees_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "getEmployees");
    private final static QName _UpdateEmployee_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "updateEmployee");
    private final static QName _DeleteEmployeeResponse_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "deleteEmployeeResponse");
    private final static QName _GetEmployeesResponse_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "getEmployeesResponse");
    private final static QName _EmployeeNotFoundException_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "EmployeeNotFoundException");
    private final static QName _AddEmployee_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "addEmployee");
    private final static QName _DeleteEmployee_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "deleteEmployee");
    private final static QName _UpdateEmployeeResponse_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "updateEmployeeResponse");
    private final static QName _EmployeeAlreadyExistsException_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "EmployeeAlreadyExistsException");
    private final static QName _GetEmployee_QNAME = new QName("http://service.employeeservice.web_services.anonbnr.org/", "getEmployee");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.anonbnr.web_services.employeeservice.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Count }
     * 
     */
    public Count createCount() {
        return new Count();
    }

    /**
     * Create an instance of {@link GetEmployees }
     * 
     */
    public GetEmployees createGetEmployees() {
        return new GetEmployees();
    }

    /**
     * Create an instance of {@link CountResponse }
     * 
     */
    public CountResponse createCountResponse() {
        return new CountResponse();
    }

    /**
     * Create an instance of {@link AddEmployeeResponse }
     * 
     */
    public AddEmployeeResponse createAddEmployeeResponse() {
        return new AddEmployeeResponse();
    }

    /**
     * Create an instance of {@link GetEmployeeResponse }
     * 
     */
    public GetEmployeeResponse createGetEmployeeResponse() {
        return new GetEmployeeResponse();
    }

    /**
     * Create an instance of {@link DeleteEmployeeResponse }
     * 
     */
    public DeleteEmployeeResponse createDeleteEmployeeResponse() {
        return new DeleteEmployeeResponse();
    }

    /**
     * Create an instance of {@link UpdateEmployee }
     * 
     */
    public UpdateEmployee createUpdateEmployee() {
        return new UpdateEmployee();
    }

    /**
     * Create an instance of {@link GetEmployee }
     * 
     */
    public GetEmployee createGetEmployee() {
        return new GetEmployee();
    }

    /**
     * Create an instance of {@link EmployeeAlreadyExistsException }
     * 
     */
    public EmployeeAlreadyExistsException createEmployeeAlreadyExistsException() {
        return new EmployeeAlreadyExistsException();
    }

    /**
     * Create an instance of {@link DeleteEmployee }
     * 
     */
    public DeleteEmployee createDeleteEmployee() {
        return new DeleteEmployee();
    }

    /**
     * Create an instance of {@link UpdateEmployeeResponse }
     * 
     */
    public UpdateEmployeeResponse createUpdateEmployeeResponse() {
        return new UpdateEmployeeResponse();
    }

    /**
     * Create an instance of {@link GetEmployeesResponse }
     * 
     */
    public GetEmployeesResponse createGetEmployeesResponse() {
        return new GetEmployeesResponse();
    }

    /**
     * Create an instance of {@link EmployeeNotFoundException }
     * 
     */
    public EmployeeNotFoundException createEmployeeNotFoundException() {
        return new EmployeeNotFoundException();
    }

    /**
     * Create an instance of {@link AddEmployee }
     * 
     */
    public AddEmployee createAddEmployee() {
        return new AddEmployee();
    }

    /**
     * Create an instance of {@link Employee }
     * 
     */
    public Employee createEmployee() {
        return new Employee();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmployeeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "getEmployeeResponse")
    public JAXBElement<GetEmployeeResponse> createGetEmployeeResponse(GetEmployeeResponse value) {
        return new JAXBElement<GetEmployeeResponse>(_GetEmployeeResponse_QNAME, GetEmployeeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddEmployeeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "addEmployeeResponse")
    public JAXBElement<AddEmployeeResponse> createAddEmployeeResponse(AddEmployeeResponse value) {
        return new JAXBElement<AddEmployeeResponse>(_AddEmployeeResponse_QNAME, AddEmployeeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "countResponse")
    public JAXBElement<CountResponse> createCountResponse(CountResponse value) {
        return new JAXBElement<CountResponse>(_CountResponse_QNAME, CountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Count }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "count")
    public JAXBElement<Count> createCount(Count value) {
        return new JAXBElement<Count>(_Count_QNAME, Count.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmployees }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "getEmployees")
    public JAXBElement<GetEmployees> createGetEmployees(GetEmployees value) {
        return new JAXBElement<GetEmployees>(_GetEmployees_QNAME, GetEmployees.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateEmployee }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "updateEmployee")
    public JAXBElement<UpdateEmployee> createUpdateEmployee(UpdateEmployee value) {
        return new JAXBElement<UpdateEmployee>(_UpdateEmployee_QNAME, UpdateEmployee.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteEmployeeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "deleteEmployeeResponse")
    public JAXBElement<DeleteEmployeeResponse> createDeleteEmployeeResponse(DeleteEmployeeResponse value) {
        return new JAXBElement<DeleteEmployeeResponse>(_DeleteEmployeeResponse_QNAME, DeleteEmployeeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmployeesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "getEmployeesResponse")
    public JAXBElement<GetEmployeesResponse> createGetEmployeesResponse(GetEmployeesResponse value) {
        return new JAXBElement<GetEmployeesResponse>(_GetEmployeesResponse_QNAME, GetEmployeesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmployeeNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "EmployeeNotFoundException")
    public JAXBElement<EmployeeNotFoundException> createEmployeeNotFoundException(EmployeeNotFoundException value) {
        return new JAXBElement<EmployeeNotFoundException>(_EmployeeNotFoundException_QNAME, EmployeeNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddEmployee }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "addEmployee")
    public JAXBElement<AddEmployee> createAddEmployee(AddEmployee value) {
        return new JAXBElement<AddEmployee>(_AddEmployee_QNAME, AddEmployee.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteEmployee }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "deleteEmployee")
    public JAXBElement<DeleteEmployee> createDeleteEmployee(DeleteEmployee value) {
        return new JAXBElement<DeleteEmployee>(_DeleteEmployee_QNAME, DeleteEmployee.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateEmployeeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "updateEmployeeResponse")
    public JAXBElement<UpdateEmployeeResponse> createUpdateEmployeeResponse(UpdateEmployeeResponse value) {
        return new JAXBElement<UpdateEmployeeResponse>(_UpdateEmployeeResponse_QNAME, UpdateEmployeeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmployeeAlreadyExistsException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "EmployeeAlreadyExistsException")
    public JAXBElement<EmployeeAlreadyExistsException> createEmployeeAlreadyExistsException(EmployeeAlreadyExistsException value) {
        return new JAXBElement<EmployeeAlreadyExistsException>(_EmployeeAlreadyExistsException_QNAME, EmployeeAlreadyExistsException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmployee }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.employeeservice.web_services.anonbnr.org/", name = "getEmployee")
    public JAXBElement<GetEmployee> createGetEmployee(GetEmployee value) {
        return new JAXBElement<GetEmployee>(_GetEmployee_QNAME, GetEmployee.class, null, value);
    }

}
