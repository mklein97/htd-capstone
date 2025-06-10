import "bootstrap/dist/js/bootstrap.bundle.min.js";
import { useEffect, useState } from "react";
import { Course, Category } from "./Models";
import { Link, useNavigate, useParams } from "react-router-dom";
import FetchWrapper from "./FetchWrapper";

interface CourseFormProps {
    requiredRole: string;
}

const CourseForm: React.FC<CourseFormProps> = ({ requiredRole }) => {

    const navigate = useNavigate();
    const { courseId } = useParams();

    const userRole = localStorage.getItem("roleName");
    if (userRole !== requiredRole) {
        navigate("/unauthorized");
    }

    const [categoryData, setCategoryData] = useState<Category[]>([]);
    const [category, setCategory] = useState<Category>(
        new Category(0, "", "", "")
    );
    const [course, setCourse] = useState<Course>(
        new Course("", "", 0, 0, category)
    );
    const [errors, setErrors] = useState([]);

    useEffect(() => {
        // Resset form 
        setCourse(new Course("", "", 0, 0, category))

        let request = {
            method: "GET",
        };
        FetchWrapper("http://localhost:8080/api/categories", request)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject("Unexpected Status Code: " + response.status);
                }
            })
            .then((data) => {
                const newData = [];
                for (const key in data) {
                    let cat: Category = JSON.parse(JSON.stringify(data[key]));
                    newData.push(cat);
                }
                setCategoryData(newData);
            })
            .catch(console.log);

        // When Editing
        if (courseId) {
            fetch(`http://localhost:8080/api/courses/${courseId}`, request)
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        return Promise.reject("Unexpected Status Code: " + response.status);
                    }
                })
                .then(data => {
                    if (data.courseId) {
                        setCourse(data);
                    }
                })
                .catch(console.log)
        }
    }, [courseId])

    const postCourse = () => {
        const init = {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(course)
        }

        FetchWrapper("http://localhost:8080/api/courses", init)
            .then(response => {
                if (response.ok || response.status === 400) {
                    return response.json();
                } else {
                    return Promise.reject("Unexpected status code: " + response.status);
                }
            })
            .then(data => {
                if (data !== undefined && data["courseId"] > 0) {
                    navigate("/courses");
                } else {
                    setErrors(data);
                }
            })
            .catch(console.log)
    }

    const putCourse = () => {
        const init = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(course)
        }

        FetchWrapper(`http://localhost:8080/api/courses/${courseId}`, init)
            .then(response => {
                if (!response.ok) {
                    return Promise.reject("Unexpected status code: " + response.status);
                }
            })
            .then(() => navigate(`/user-details/${localStorage.getItem("userId")}`))
            .catch(console.log)
    }

    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = event.target;

        if (name === "categories") {
            const selectedCategory = categoryData.find(cat => cat.categoryId === Number(value));
            if (selectedCategory) {
                setCategory(selectedCategory);
                setCourse(prev => ({ ...prev, category: selectedCategory }));
            }
        } else if (name === "courseName") {
            setCourse(prev => ({ ...prev, courseName: value }));
        } else if (name === "courseDescription") {
            setCourse(prev => ({ ...prev, courseDescription: value }));
        } else if (name === "price") {
            setCourse(prev => ({ ...prev, price: Number(value) }));
        } else if (name === "estimatedDuration") {
            setCourse(prev => ({ ...prev, estimateDuration: Number(value) }));
        }
    }

    const onSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (courseId) {
            putCourse();
        } else {
            postCourse();
        }
    }

    return (<>
        <div className="container my-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card shadow-sm">
                        <div className="card-body">
                            <h2 className="card-title text-center mb-4">{courseId ? "Edit Course" : "Add Course"}</h2>
                            <form onSubmit={onSubmit} id="courseForm">
                                <div className="mb-3">
                                    <ul>
                                        {errors.map((e: String) =>
                                            <li key={e.length + (Math.random() * 100)} style={{ color: "lightcoral" }}>{e}</li>
                                        )}
                                    </ul>
                                </div>
                                {/* Course Name */}
                                <div className="mb-3">
                                    <label htmlFor="courseName" className="form-label">Course Name</label>
                                    <input type="text" className="form-control" id="courseName" name="courseName"
                                        onChange={handleChange} value={course.courseName} placeholder="Introduction to Accounting" required/>
                                </div>
                                {/* Categories Menu*/}
                                <div className="mb-3">
                                    <label className="form-label" htmlFor="categories">Categories</label>
                                    <select className="form-select" id="categories" onChange={handleChange} aria-label="select menu"
                                        name="categories" value={course.category.categoryId || ""} required>
                                        <option value="" disabled selected >Select Course Category</option>
                                        {categoryData.map(cat =>
                                            <option key={cat.categoryId} value={cat.categoryId}>{cat.categoryName}</option>
                                        )}
                                    </select>
                                </div>
                                {/* Course Description */}
                                <div className="mb-3">
                                    <label className="form-label" htmlFor="courseDescription">Description</label>
                                    <textarea className="form-control" id="courseDescription" onChange={handleChange} style={{ height: "200px" }}
                                        name="courseDescription" value={course.courseDescription} placeholder="Class description goes here..." required></textarea>
                                </div>
                                {/* Price */}
                                <div className="mb-3">
                                    <label htmlFor="price" className="form-label">Price</label>
                                    <input type="number" className="form-control" id="price" name="price"
                                        min={1} max={1000} onChange={handleChange}
                                        value={course.price === 0 ? "" : course.price} placeholder="10" required/>
                                </div>
                                {/* Duration */}
                                <div className="mb-3">
                                    <label htmlFor="duration" className="form-label">Estimate Duration (Hours)</label>
                                    <input type="number" className="form-control" id="duration" name="estimatedDuration"
                                        min={1} max={100} onChange={handleChange}
                                        value={course.estimateDuration === 0 ? "" : course.estimateDuration} placeholder="20" required/>
                                </div>
                                <button
                                    type="submit"
                                    className="btn btn-primary mt-4 mr-3"
                                >
                                    {courseId ? "Edit Course" : "Create Course"}
                                </button>
                                <Link to={'/'} className={'btn btn-secondary mt-4 ms-3'}>Cancel</Link>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </>)
}

export default CourseForm;