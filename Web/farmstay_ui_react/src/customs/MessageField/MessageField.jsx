import PropTypes from 'prop-types';
import Form from 'react-bootstrap/Form';

MessageField.propTypes = {
    field: PropTypes.object.isRequired,
    form: PropTypes.object.isRequired,

    type: PropTypes.string,
    placeholder: PropTypes.string,
    disabled: PropTypes.bool,
};

MessageField.defaultProps = {
    type: 'text',
    label: '',
    placeholder: '',
    description: '',
    disabled: false,
};

function MessageField({ field, form, type, placeholder, description, disabled, ...props }) {
    const { name, value, onChange, onBlur } = field;
    // const { errors, touched } = form;
    return (
        <Form.Group>
            <Form.Control
                id={name}
                type={type}
                name={name}
                value={value}
                onChange={onChange}
                onBlur={onBlur}
                disabled={disabled}
                placeholder={placeholder}
                className="shadow-none"
                autoComplete="off"
            />
        </Form.Group>
    );
}

export default MessageField;
