package Image;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
/**
 *  GO TO LINE 150 AND YOU WILL FIND THE ROOT AND ALL YOU WANT TO KNOW . .
 */
public class ImageSearch extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final int MAX_IMAGE_SIZE = 200;
    private static final int MAX_IMAGE_COUNT = 30;
    private static final int COLOR_DEPTH = 32;

    // Text field for image path
    private final JTextField imageField;
    // Button to browse image file
    private final JButton imageButton;
    // Button to resize image
    private final JButton resizeButton;
    // Button to crop image
    private final JButton cropButton;

    // List of selected folder paths
    private List<String> folderPaths;
    // Button to browse folders
    private final JButton folderButton;
    // Panel to display search results
    private final JPanel resultsPanel;

    // Radio buttons for search options
    private final JRadioButton similarityRadioButton;
    private final JRadioButton colorRadioButton;
    private final JRadioButton sizedRadiaButton;
    private final JRadioButton dateRadioButton;

    // File chooser for saving cropped image
    private final JFileChooser saveFileChooser;

    // Button to initiate search
    private final JButton searchButton;

    // List of image paths
    List<String> imagePaths = new ArrayList<>();

    // Selected search option
    private int selectedOption = 0;
    // Flag to check if folder is selected
    private Boolean choosedFolder = false;

    public ImageSearch() {
        super("Image Search ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout());

        // Create an input panel at the top of the frame
        JPanel inputPanel = new JPanel(new FlowLayout());

        // Create components for selecting an image file
        JLabel imageLabel = new JLabel("Image:");
        imageField = new JTextField();
        imageField.setPreferredSize(new Dimension(500, 25));
        imageButton = new JButton("Browse");
        imageButton.addActionListener(this);
        resizeButton = new JButton("Resize");
        resizeButton.addActionListener(this);
        cropButton = new JButton("Crop");
        cropButton.addActionListener(this);

        // Add the image components to the input panel
        inputPanel.add(resizeButton);
        inputPanel.add(cropButton);
        inputPanel.add(imageLabel);
        inputPanel.add(imageField);
        inputPanel.add(imageButton);

        // Create components for selecting folder paths
        JLabel folderLabel = new JLabel("Folders:");
        folderButton = new JButton("Browse");
        folderButton.addActionListener(this);

        // Add the folder components to the input panel
        inputPanel.add(Box.createHorizontalStrut(20));
        inputPanel.add(folderLabel);
        inputPanel.add(Box.createHorizontalStrut(5));
        inputPanel.add(folderButton);

        // Add the input panel to the top of the frame
        add(inputPanel, BorderLayout.NORTH);

        // Create a panel for the options and search button
        JPanel optionsAndSearchPanel = new JPanel();
        optionsAndSearchPanel.setLayout(new BorderLayout());

        JPanel optionsPanel = new JPanel();
        similarityRadioButton = new JRadioButton("Similarity");
        colorRadioButton = new JRadioButton("Color");
        dateRadioButton = new JRadioButton("Date");
        sizedRadiaButton = new JRadioButton("Size");
        saveFileChooser = new JFileChooser();
        searchButton = new JButton("Search");
        similarityRadioButton.addActionListener(this);
        colorRadioButton.addActionListener(this);
        sizedRadiaButton.addActionListener(this);
        dateRadioButton.addActionListener(this);
        searchButton.addActionListener(this);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(resizeButton);
        buttonGroup.add(similarityRadioButton);
        buttonGroup.add(colorRadioButton);
        buttonGroup.add(sizedRadiaButton);
        buttonGroup.add(dateRadioButton);




        optionsPanel.add(Box.createHorizontalStrut(5));

        optionsPanel.add(similarityRadioButton);
        optionsPanel.add(sizedRadiaButton);
        optionsPanel.add(searchButton);
        optionsPanel.add(dateRadioButton);
        optionsPanel.add(colorRadioButton);

        // Add the options panel to the bottom of the frame
        add(optionsPanel, BorderLayout.SOUTH);

        // Create a panel for displaying search results
        resultsPanel = new JPanel(new GridLayout(0, 1));

        // Add the results panel to a scroll pane and add it to the center of the frame
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Make the frame visible
        setVisible(true);
    }

    /**
     * HERE IS THE ROOT
     *  من هنا فينا نوصل لأي تابع بدنا ياه
     *  يلي عم يصير هو التالي :
     *  actionPerformed(ActionEvent e)
     *  يعني شو رح يصير وقت نكبس الزر
     */
    public void actionPerformed(ActionEvent e) {
        /**
         *هنا بمعنى.. ماذا سيحصل عندما اضغط على زر ال Image Button  ؟
         * رح نستدعي تابع الImageSelect();
         * يلي هو شغلته اختيار الصورة
         */
        if (e.getSource() == imageButton) {
            ImageSelect();
        }
        /**
         *هنا بمعنى.. ماذا سيحصل عندما اضغط على زر ال folderButton  ؟
         * رح نستدعي تابع الFolderSelect()
         * يلي هو شغلته اختيار المجلد
         *
         * وهكذا باقي حالات ال else if
         * بحسب اسم الزر المحدد نستدعي التابع المحدد
         */
        else if (e.getSource() == folderButton) {
            choosedFolder=true;
            FolderSelect();
        } else if (e.getSource() == similarityRadioButton) {
            selectedOption=1;

        }else if (e.getSource() == sizedRadiaButton) {
            selectedOption=3;

        } else if (e.getSource() == dateRadioButton) {
            selectedOption=4;

        }
         else if (e.getSource() == colorRadioButton) {
            selectedOption = 2;
        }else if (e.getSource() == cropButton) {
            CropImage();
            savedCropIm();
        }else if (e.getSource() == resizeButton) {
            ResizeAndSaveImage();
        }



        /**
         *هنا بمعنى.. ماذا سيحصل عندما اضغط على زر ال Search؟
         * حينها حسب ااختيارنا لطريقة البحث..تكون النتيجة
         **/
        else if (e.getActionCommand().equals("Search")) {
            if (!choosedFolder ){
                JOptionPane.showMessageDialog(this,
                        "Please Select Folder..!",
                        "Welcome to our Image Search", JOptionPane.WARNING_MESSAGE);

            }
             if(selectedOption==1){
                 SimilarSearch();
             }
             else if(selectedOption==2){
                 ColorSearch();
             }else if (selectedOption == 3) {
                 SizeSearch();
             } else if (selectedOption == 4) {
                 DateSearch();
             }

             else {SimilarSearch();}

        }



    }

    private void ImageSelect() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imageField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void FolderSelect() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            folderPaths = new ArrayList<>();
            for (File folder : fileChooser.getSelectedFiles()) {
                folderPaths.add(folder.getAbsolutePath());
            }
            // Perform search

        }

    }

    private void SimilarSearch() {
        // Clear previous results
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();
        // Perform search
        searchBySimilarity();
    }

    private void ColorSearch() {
        // Clear previous results
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();
        // Perform search
        searchByColor();
    }

    private void DateSearch() {
        // Clear previous results
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();

        // Perform search
        searchByDate();
    }
    /**
     * Handles the search by image size.
     */
    private void SizeSearch() {
        // Clear previous results
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();

        // Perform search
        SearchByImSize();

    }

    private void CropImage() {
        String imagePath = imageField.getText();
        if (!imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                try {
                    BufferedImage originalImage = ImageIO.read(imageFile);
                    BufferedImage croppedImage = cropImage(originalImage);

                    DisplayCropIm(croppedImage);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error Loading Image",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Image Does Not Exist",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Image Path Is Empty",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BufferedImage cropImage(BufferedImage image) {
        Component frame = new JFrame();
        String widthString = JOptionPane.showInputDialog(frame, "Enter the desired width:");
        String heightString = JOptionPane.showInputDialog(frame, "Enter the desired height:");

        int width, height;
        try {
            width = Integer.parseInt(widthString);
            height = Integer.parseInt(heightString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid width or height!");
            return null;
        }

        int x = Math.max(0, (image.getWidth() - width) / 2);
        int y = Math.max(0, (image.getHeight() - height) / 2);
        int cropWidth = Math.min(width, image.getWidth() - x);
        int cropHeight = Math.min(height, image.getHeight() - y);

        BufferedImage croppedImage = image.getSubimage(x, y, cropWidth, cropHeight);
        return croppedImage;
    }

    private void DisplayCropIm(BufferedImage croppedImage) {
        // Clear previous results
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();

        // Create a label for the cropped image and add it to the results panel
        JLabel imageLabel = new JLabel(new ImageIcon(croppedImage));

        JPanel resultPanel = new JPanel(new FlowLayout());

        resultPanel.add(imageLabel);
        resultsPanel.add(resultPanel);

        // Refresh and repaint the results panel to display the updated image
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void savedCropIm() {
        BufferedImage croppedImage = getCroppedImageFromResultsPanel();
        if (croppedImage == null) {
            JOptionPane.showMessageDialog(this,
                    "No cropped image available",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // open file chooser to choose where to save the file
        int result = saveFileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File outputFile = saveFileChooser.getSelectedFile();
            String outputFilePath = outputFile.getAbsolutePath();

            try {
                ImageIO.write(croppedImage, "png", new File(outputFilePath));
                JOptionPane.showMessageDialog(this,
                        "Image saved successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error saving image",
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private BufferedImage getCroppedImageFromResultsPanel() {
        Component[] components = resultsPanel.getComponents();
        if (components.length > 0 && components[0] instanceof JPanel) {
            JPanel resultPanel = (JPanel) components[0];
            Component[] resultComponents = resultPanel.getComponents();
            if (resultComponents.length > 0 && resultComponents[0] instanceof JLabel) {
                JLabel imageLabel = (JLabel) resultComponents[0];
                Icon icon = imageLabel.getIcon();
                if (icon instanceof ImageIcon) {
                    Image image = ((ImageIcon) icon).getImage();
                    if (image instanceof BufferedImage) {
                        return (BufferedImage) image;
                    }
                }
            }
        }
        return null;
    }


    private void ResizeAndSaveImage() {
        String imagePath = imageField.getText();
        if (!imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Component frame = new JFrame();
                String widthString = JOptionPane.showInputDialog(frame, "Enter the desired width:");
                String heightString = JOptionPane.showInputDialog(frame, "Enter the desired height:");

                int width, height;
                try {
                    width = Integer.parseInt(widthString);
                    height = Integer.parseInt(heightString);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, "Invalid width or height!");
                    return;
                }

                try {
                    BufferedImage originalImage = ImageIO.read(imageFile);
                    BufferedImage resizedImage = resizeImage(originalImage, width, height); // Change the dimensions as per your requirement

                    DisplayResizedIm(resizedImage); // Display the resized image

                    // Save the resized image to the desktop
                    String desktopPath = System.getProperty("user.home") + "/Desktop/";
                    String outputPath = desktopPath + "resized_image.jpg"; // Change the file format and name as needed

                    File output = new File(outputPath);
                    ImageIO.write(resizedImage, "png", output);

                    JOptionPane.showMessageDialog(this,
                            "Image saved successfully to the desktop!",
                            "Image Saved", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error Loading Image",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Image Does Not Exist",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Image Path Is Empty",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Helper method to resize an image to the specified width and height
    private BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, image.getType());
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
    }
    private void saveImage(BufferedImage image) {
        // Perform image saving logic here
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            try {
                ImageIO.write(image, ".png", new File(filePath));
                JOptionPane.showMessageDialog(this,
                        "Image saved successfully!",
                        "Image Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error saving image",
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void DisplayResizedIm(BufferedImage resizedImage) {
        // Clear previous results
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();

        // Create a label for the resized image and add it to the results panel
        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
        JPanel resultPanel = new JPanel(new FlowLayout());
        resultPanel.add(imageLabel);
        resultsPanel.add(resultPanel);

        // Refresh and repaint the results panel to display the updated image
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    /**
     * Performs the search by image Similarity.
     */
    private void searchBySimilarity() {
        String imagePath = imageField.getText();
        if (!imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                try {
                    BufferedImage queryImage = ImageIO.read(imageFile);
                    List<String> similarImages = FindSimilarIm(queryImage);
                    DisplayResult(similarImages);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error Loading Image",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();

                }
            }else { JOptionPane.showMessageDialog(this,
                    "Image Is Not Exist",
                    "Error", JOptionPane.ERROR_MESSAGE);}
        }else{ JOptionPane.showMessageDialog(this,
                "Image Path Is Empty",
                "Error", JOptionPane.ERROR_MESSAGE);}
        if (folderPaths.isEmpty()|| folderPaths==null ){
            JOptionPane.showMessageDialog(this,
                    "Please Select Folder..!",
                    "Welcome to our Image Search", JOptionPane.WARNING_MESSAGE);

        }
    }
    /**
     * Performs the search by Color.
     */
    private void searchByColor() {
        String imagePath = imageField.getText();
        if (!imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                try {
                    BufferedImage queryImage = ImageIO.read(imageFile);
                    List<String> similarImages = FindcolorMatchingImage(queryImage);
                    DisplayResult(similarImages);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error Loading Image",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();

                }
            }else { JOptionPane.showMessageDialog(this,
                    "Image Is Not Exist",
                    "Error", JOptionPane.ERROR_MESSAGE);}
        }else{ JOptionPane.showMessageDialog(this,
                "Image Path Is Empty",
                "Error", JOptionPane.ERROR_MESSAGE);}
        if (folderPaths.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please Select Folder..!",
                    "Welcome to our Image Search", JOptionPane.WARNING_MESSAGE);

        }
    }
    /**
     * Performs the search by Date Modification.
     */
    private void searchByDate() {
        String imagePath = imageField.getText();
        if (!imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                try {
                    BufferedImage queryImage = ImageIO.read(imageFile);
                    List<String> matchingImages = FindDataMatchingImages(queryImage);
                    DisplayResult(matchingImages);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error Loading Image",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Image Does Not Exist",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Image Path Is Empty",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (folderPaths.isEmpty() || folderPaths == null) {
            JOptionPane.showMessageDialog(this,
                    "Please Select Folder..!",
                    "Welcome to our Image Search", JOptionPane.WARNING_MESSAGE);
        }
    }
    /**
     * Performs the search by image size.
     */
    private void SearchByImSize() {
        String imagePath = imageField.getText();
        if (!imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                try {
                    BufferedImage queryImage = ImageIO.read(imageFile);
                    List<String> matchingImages = FindSizeMatchingImages(queryImage);
                    DisplayResult(matchingImages);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error Loading Image",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Image Does Not Exist",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Image Path Is Empty",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (folderPaths.isEmpty() || folderPaths == null) {
            JOptionPane.showMessageDialog(this,
                    "Please Select Folder..!",
                    "Welcome to our Image Search", JOptionPane.WARNING_MESSAGE);
        }
    }
    /**
     * Finds the matching images by Similar Images.
     *
     * @param image the query image
     * @return a list of matching image paths
     */
    private List<String> FindSimilarIm(BufferedImage image) {
        // Perform similarity search logic here

        ;
        // Initialize data structures for storing search results
        Map<String, Double> distances = new HashMap<>();
        imagePaths = new ArrayList<>();

        List<Double> similarityRatios = new ArrayList<>();
        QuantizeImage(image);  // Reduce color depth of the image

        double maxSimilarity = 0;  // Variable to store the maximum similarity
        String maxSimilarityPath = "";  // Variable to store the path of the image with maximum similarity

        // Iterate through the selected folders and their files
        for (String folderPath : folderPaths) {
            File folder = new File(folderPath);
            if (!folder.isDirectory()) {
                continue;
            }
            File[] files = folder.listFiles();
            if (files == null) {
                continue;
            }
            for (File file : files) {
                if (!file.isFile()) {
                    continue;
                }
                String filePath = file.getAbsolutePath();
                String extension = getFileExtension(filePath);
                if (!extension.matches("jpe?g|png|gif|bmp")) {
                    continue;
                }

                // Read each image file from the folder and store it in a BufferedImage object
                BufferedImage otherImage = null;
                try {
                    otherImage = ImageIO.read(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    continue;
                }

                QuantizeImage(otherImage);  // Reduce color depth of the other image

                // Calculate similarity between the selected image and the other image
                double similarity = CalculateSimilar(image, otherImage);
                if (similarity >= 60) {
                    // Add the image to the results if the similarity is above a threshold (90%)
                    distances.put(filePath, similarity);
                    imagePaths.add(filePath);
                    similarityRatios.add(similarity);

                    // Update the maximum similarity and its corresponding image path
                    if (similarity > maxSimilarity) {
                        maxSimilarity = similarity;
                        maxSimilarityPath = filePath;
                    }
                }
            }
        }

        // Add the original image with 100% similarity to the results
        if (!maxSimilarityPath.isEmpty()) {
            distances.put(maxSimilarityPath, 1.0);
            imagePaths.add(maxSimilarityPath);
            similarityRatios.add(100.0);
        }

        // Determine the number of images to display in the results panel
        int count = Math.min(imagePaths.size(), MAX_IMAGE_COUNT);

        if (folderPaths.isEmpty()|| folderPaths==null ){
            JOptionPane.showMessageDialog(this,
                    "Please Select Folder..!",
                    "Welcome to our Image Search", JOptionPane.WARNING_MESSAGE);

        }
        // Clear the existing results panel
        resultsPanel.removeAll();

        // Iterate through the selected images and display them in the results panel
        for (int i = 0; i < count; i++) {
            String imagePath2 = imagePaths.get(i);
            BufferedImage resultImage = null;
            try {
                resultImage = ImageIO.read(new File(imagePath2));
            } catch (IOException ex) {
                ex.printStackTrace();
                continue;
            }

            // Resize the image if its dimensions exceed the maximum size
            int width = resultImage.getWidth();
            int height = resultImage.getHeight();
            if (width > MAX_IMAGE_SIZE || height > MAX_IMAGE_SIZE) {
                double scale = Math.min((double) MAX_IMAGE_SIZE / width, (double) MAX_IMAGE_SIZE / height);
                width = (int) (width * scale);
                height = (int) (height * scale);
                resultImage = resizeImage(resultImage, width, height);
            }



            // Create labels for the image, distance, and path, and add them to a result panel
            JLabel imageLabel = new JLabel();
            imageLabel.setIcon(new javax.swing.ImageIcon(resultImage));
            JLabel distanceLabel = new JLabel(String.format("%.2f%%", similarityRatios.get(i)));
            JLabel pathLabel = new JLabel(imagePath2);
            JPanel resultPanel = new JPanel(new FlowLayout());
            resultPanel.add(distanceLabel);
            resultPanel.add(imageLabel);
            resultPanel.add(pathLabel);
            resultsPanel.add(resultPanel);
        }
        // Refresh and repaint the results panel to display the updated results
        resultsPanel.revalidate();
        resultsPanel.repaint();


        // ...
        return new ArrayList<>();
    }

    /**
     * Finds the matching images by image color.
     *
     * @param image the query image
     * @return a list of matching image paths
     */
    public List<String> FindcolorMatchingImage(BufferedImage image) {
        List<String> similarImages = new ArrayList<>();
         // Open a color chooser dialog to select a color
        Color color = JColorChooser.showDialog(null, "Select a color", Color.WHITE);
        if (color == null) {
            return similarImages;
        }
        int queryRed = color.getRed();// Get the red component of the selected color
        int queryGreen = color.getGreen();// Get the green component of the selected color
        int queryBlue = color.getBlue(); // Get the blue component of the selected color

        for (String folderPath : folderPaths) {
            File folder = new File(folderPath);
            if (!folder.isDirectory()) {
                continue;
            }

            File[] files = folder.listFiles();
            if (files == null) {
                continue;
            }

            for (File file : files) {
                if (!file.isFile()) {
                    continue;
                }

                String filePath = file.getAbsolutePath();
                String extension = getFileExtension(filePath);
                if (!extension.matches("jpe?g|png|gif|bmp")) {
                    continue;
                }

                try {
                     image = ImageIO.read(file);

                    int width = image.getWidth();
                    int height = image.getHeight();

                    int numPixels = width * height;
                    int[] pixels = new int[numPixels];
                    image.getRGB(0, 0, width, height, pixels, 0, width);

                    int colorSimilarityCount = 0;

                    for (int i = 0; i < numPixels; i++) {
                        int pixel = pixels[i];
                        int red = (pixel >> 16) & 0xFF;
                        int green = (pixel >> 8) & 0xFF;
                        int blue = pixel & 0xFF;

                        // Calculate the Euclidean distance between the query color and the current pixel color
                        double distance = Math.sqrt(
                                Math.pow(queryRed - red, 2) +
                                        Math.pow(queryGreen - green, 2) +
                                        Math.pow(queryBlue - blue, 2)
                        );

                        // Set a threshold value for color similarity
                        if (distance < 50) {
                            colorSimilarityCount++;
                        }
                    }

                    // If a sufficient number of pixels have similar colors, consider it a match
                    if (colorSimilarityCount > numPixels * 0.5) {
                        similarImages.add(filePath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }





        return similarImages;
    }
    /**
     * Finds the matching images by Date Modification.
     *
     * @param image the query image
     * @return a list of matching image paths
     */
    private List<String> FindDataMatchingImages(BufferedImage image) {
        List<String> matchingImages = new ArrayList<>();
        long queryLastModified = new File(imageField.getText()).lastModified();

        for (String folderPath : folderPaths) {
            File folder = new File(folderPath);
            if (!folder.isDirectory()) {
                continue;
            }

            File[] files = folder.listFiles();
            if (files == null) {
                continue;
            }

            for (File file : files) {
                if (!file.isFile()) {
                    continue;
                }

                long fileLastModified = file.lastModified();
                if (fileLastModified >= queryLastModified) {
                    matchingImages.add(file.getAbsolutePath());
                }
            }
        }

        return matchingImages;
    }
    /**
     * Finds the matching images by image size.
     *
     * @param image the query image
     * @return a list of matching image paths
     */
    private List<String> FindSizeMatchingImages(BufferedImage image) {
        List<String> matchingImages = new ArrayList<>();
        int queryWidth = image.getWidth();
        int queryHeight = image.getHeight();

        for (String folderPath : folderPaths) {
            File folder = new File(folderPath);
            if (!folder.isDirectory()) {
                continue;
            }

            File[] files = folder.listFiles();
            if (files == null) {
                continue;
            }

            for (File file : files) {
                if (!file.isFile()) {
                    continue;
                }

                String filePath = file.getAbsolutePath();
                String extension = getFileExtension(filePath);
                if (!extension.matches("jpe?g|png|gif|bmp")) {
                    continue;
                }

                try {
                    BufferedImage otherImage = ImageIO.read(file);
                    int otherWidth = otherImage.getWidth();
                    int otherHeight = otherImage.getHeight();

                    if (Math.abs(queryWidth - otherWidth) <= 10 && Math.abs(queryHeight - otherHeight) <= 10) {
                        matchingImages.add(filePath);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return matchingImages;
    }


    // Helper method to display the matching images in the results panel
    private void DisplayResult(List<String> imagePaths) {
        for (String imagePath : imagePaths) {
            JLabel imageLabel = new JLabel();
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image scaledImage = imageIcon.getImage().getScaledInstance(MAX_IMAGE_SIZE, MAX_IMAGE_SIZE, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            resultsPanel.add(imageLabel);
        }  if (imagePaths.size()<1){
            JOptionPane.showMessageDialog(this,
                    "No matching images found.",
                    "No Images Matched",
                    JOptionPane.WARNING_MESSAGE);


        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    // For the similary :
    /**
     * Reduce the color depth of the image by rounding the RGB values to the nearest multiple of COLOR_DEPTH.
     * @param image The image to quantize.
     */
    private void QuantizeImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = pixel & 0xFF;
            r = (r / COLOR_DEPTH) * COLOR_DEPTH;
            g = (g / COLOR_DEPTH) * COLOR_DEPTH;
            b = (b / COLOR_DEPTH) * COLOR_DEPTH;
            pixels[i] = (r << 16) | (g << 8) | b;
        }

        image.setRGB(0, 0, width, height, pixels, 0, width);
    }

    /**
     * Calculate the similarity between two images using the Manhattan distance formula.
     * @param image1 The first image.
     * @param image2 The second image.
     * @return The similarity ratio between the two images.
     */
    private double CalculateSimilar(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image1.getHeight();

        // Resize image2 to match the dimensions of image1
        BufferedImage resizedImage2 = resizeImage(image2, width, height);

        int[] pixels1 = image1.getRGB(0, 0, width, height, null, 0, width);
        int[] pixels2 = resizedImage2.getRGB(0, 0, width, height, null, 0, width);

        int distance = 0;
        for (int i = 0; i < pixels1.length; i++) {
            int pixel1 = pixels1[i];
            int pixel2 = pixels2[i];
            int r1 = (pixel1 >> 16) & 0xFF;
            int g1 = (pixel1 >> 8) & 0xFF;
            int b1 = pixel1 & 0xFF;
            int r2 = (pixel2 >> 16) & 0xFF;
            int g2 = (pixel2 >> 8) & 0xFF;
            int b2 = pixel2 & 0xFF;
            distance += Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
        }

        int maxDistance = width * height * COLOR_DEPTH;
        double similarity = 100 - (distance * 100.0 / maxDistance);
        return similarity;
    }

    /**
     * Resize the given image to the specified width and height.
     * @param image The image to resize.
     * @param width The new width.
     * @param height The new height.
     * @return The resized image.
     */


    /**
     * Get the extension of a file.
     * @param filePath The file path.
     * @return The file extension.
     */
    private String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < filePath.length() - 1) {
            return filePath.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageSearch());
    }
}

