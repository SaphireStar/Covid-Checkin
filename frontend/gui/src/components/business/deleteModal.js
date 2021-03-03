import React, { useState } from 'react';
import {Button, Modal} from 'react-bootstrap';
import '../../css/business.css';

/**
 * Modal used to confirm business deletion
 */
const DeleteModal = ({ business, deleteHandler }) => {
    const [show, setShow] = useState(false);
  
    const handleClose = () => setShow(false);
    const handleConfirm = () => {
        deleteHandler();
        setShow(false);
    };
    const handleShow = () => setShow(true);
  
    return (
      <>
      <div class="deleteButtonWrapper">
        <Button variant="secondary" onClick={handleShow}>
          Delete
        </Button>
  
        <Modal
          show={show}
          onHide={handleClose}
          backdrop="static"
          keyboard={false}
        >
          <Modal.Header closeButton>
            <Modal.Title>Confirmation</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            Are you sure you want to permanently delete {business}?
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Cancel
            </Button>
            <Button variant="primary" onClick={handleConfirm}>Delete</Button>
          </Modal.Footer>
        </Modal>
        </div>
      </>
    );
  }
  
  export default DeleteModal;