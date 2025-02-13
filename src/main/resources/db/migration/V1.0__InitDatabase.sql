CREATE TABLE application_settings
(
    id                        BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name                      VARCHAR(255)          NOT NULL UNIQUE,
    are_default_settings      BOOLEAN               NOT NULL,
    iterations_min            INT                   NOT NULL,
    iterations_max            INT                   NOT NULL,
    iterations_preview        INT                   NOT NULL,
    iterations_export         INT                   NOT NULL,
    export_height             INT                   NOT NULL,
    export_width              INT                   NOT NULL,
    number_of_threads         INT                   NOT NULL,
    min_chunk_border_size     INT                   NOT NULL,
    max_chunk_border_size     INT                   NOT NULL,
    default_rescale_on_scroll DOUBLE                NOT NULL

);

CREATE TABLE color_settings
(
    id                                     BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name                                   VARCHAR(255)          NOT NULL UNIQUE,
    are_default_settings                   BOOLEAN               NOT NULL,
    background_color                       VARCHAR(255)          NOT NULL,
    included_elements_color                VARCHAR(255)          NOT NULL,
    boundary_gradient_start_color          VARCHAR(255)          NOT NULL,
    boundary_gradient_end_color            VARCHAR(255)          NOT NULL,
    fade_out                               BOOLEAN               NOT NULL,
    min_iterations_satisfied_to_be_visible INT                   NOT NULL
);



CREATE TABLE recurrent_expression
(
    id                       BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name                     VARCHAR(255)          NOT NULL,
    first_expression         VARCHAR(255)          NOT NULL,
    recurrent_expression     VARCHAR(255)          NOT NULL,
    position_variable_name   VARCHAR(255)          NOT NULL,
    recurrent_variable_name  VARCHAR(255)          NOT NULL,
    default_view_location_id BIGINT                NOT NULL
);

CREATE TABLE view_location
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name            VARCHAR(255)          NOT NULL,
    center_point    BLOB                  NOT NULL,
    horizontal_span DOUBLE                NOT NULL
);

CREATE TABLE expression_locations
(
    expression_id BIGINT NOT NULL,
    location_id   BIGINT NOT NULL,
    CONSTRAINT PRIMARY KEY (expression_id, location_id)
);



ALTER TABLE expression_locations
    ADD CONSTRAINT UNIQUE (location_id);

ALTER TABLE recurrent_expression
    ADD CONSTRAINT FOREIGN KEY (default_view_location_id) REFERENCES view_location (id);

ALTER TABLE expression_locations
    ADD CONSTRAINT FOREIGN KEY (expression_id) REFERENCES recurrent_expression (id);

ALTER TABLE expression_locations
    ADD CONSTRAINT FOREIGN KEY (location_id) REFERENCES view_location (id);