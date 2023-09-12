import tensorflow as tf
from tensorflow import keras

# Load the trained model
loaded_model = keras.models.load_model('trained_lstm_model.keras')

# Convert the model to TensorFlow Lite format
converter = tf.lite.TFLiteConverter.from_keras_model(loaded_model)

# Add these lines to set supported_ops and disable lower_tensor_list_ops
converter.target_spec.supported_ops = [tf.lite.OpsSet.TFLITE_BUILTINS, tf.lite.OpsSet.SELECT_TF_OPS]
converter._experimental_lower_tensor_list_ops = False

tflite_model = converter.convert()

# Save the TensorFlow Lite model to a file
with open('trained_lstm_model.tflite', 'wb') as f:
    f.write(tflite_model)

