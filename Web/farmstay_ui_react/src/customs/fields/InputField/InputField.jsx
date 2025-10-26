import { ErrorMessage } from 'formik';
import PropTypes from 'prop-types';
import Form from 'react-bootstrap/Form';

InputField.propTypes = {
    field: PropTypes.object.isRequired,
    form: PropTypes.object.isRequired,

    type: PropTypes.string,
    label: PropTypes.string,
    placeholder: PropTypes.string,
    description: PropTypes.string,
    disabled: PropTypes.bool,
};

InputField.defaultProps = {
    type: 'text',
    label: '',
    placeholder: '',
    description: '',
    disabled: false,
};

function InputField({ field, form, type, label, placeholder, description, disabled, ...props }) {
    const { name, value, onChange, onBlur } = field;
    const { errors, touched } = form;
    const showError = !!errors[name] && !!touched[name];
    return (
        <Form.Group className="mb-1">
            {label && (
                <Form.Label htmlFor={name} className="fw-semibold">
                    {label}
                </Form.Label>
            )}
            <Form.Control
                id={name}
                type={type}
                name={name}
                value={value}
                onChange={onChange}
                onBlur={onBlur}
                disabled={disabled}
                placeholder={placeholder}
                isInvalid={showError}
                isValid={!!touched[name] && !errors[name]}
            />
            <ErrorMessage
                name={name}
                component={(errorMessage) => {
                    return <Form.Control.Feedback type="invalid">{errorMessage.children}</Form.Control.Feedback>;
                }}
            />
            {/* {showError ? (
                <p muted style={{ fontSize: '0.75rem', color: 'red', marginTop: '4px' }}>
                    {errors[name]}
                </p>
            ) : (
                <Form.Text muted style={{ fontSize: '0.75rem', marginTop: '4px' }}>
                    {description}
                </Form.Text>
            )} */}
        </Form.Group>
    );
}

export default InputField;
