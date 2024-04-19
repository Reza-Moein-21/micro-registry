# Micro Service Registry

## Available Features

- Register Service
- Registration request AKA HeartBeat
- Give service registration information
- Remove service from registry

## Service Model

    {service-name,host,port,?entry}
    Ex. {my-service,my-service,443,/info}

- service-name
    - Mandatory
    - MUST be US_ASCII
    - MUST start with letter
    - MUST only include letter, number or dash
    - Maximum length is 30 characters
    - Minimum length is 2 characters
- host
    - Mandatory
    - Must be domain name or IP
    - Maximum length is 30 characters
    - Minimum length is 2 characters
- port
    - Mandatory
    - Must be only digit
    - Maximum port number is 65535
    - Minimum port number is 1
- entry
    - Optional
    - Maximum length is 100 characters
    - Considered as service entry uri

## Request Format

	<Type>[<data>]

Available Request Types

- Heart Beat
- Fetch
- Remove

## Response Format

	[<data>]<Status>

Available Response Status

- Error

      [{<code>,?<message>}]Error

- Ok

      [<data>]Ok

---

## Heart Beat Request

	HeartBeat[<data>]

### Heart Beat data format

    {<service-name>,<host>,<port>,?<entry>}

## Heart Beat Response

- Ok

      [{<service-name>,<status>}]Ok

- Error

      [<error_code>]Error

### available error types

- SERVER_ERROR,

---

## Fetch Request

    Fetch[{<search-param>}]

- take an array of parameters
- max array size MUST be 10

### Fetch search param format

    {<filed_name>,<field_value>,<type>}

- available field names
    - service-name
    - hostname
    - path
- search type
    - e (Exact match)
    - l (Like match)

## Fetch Response

- Ok

      [{service-model}]Ok

- Error

      [???]Error

## Remove Request

    Remove[{service-name}]

- accept an array of service names

## Remove Response

- Ok

      [{service-name}]Ok

- Error

      [???]Error
